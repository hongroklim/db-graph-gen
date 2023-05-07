package dev.rokong.dbgraphgen.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.rokong.dbgraphgen.entity.DbObj;
import dev.rokong.dbgraphgen.entity.Dependency;
import dev.rokong.dbgraphgen.vo.DerbyConnVO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class DerbyRepositoryImpl implements DerbyRepository {
    private static final String driverClassName = "org.apache.derby.jdbc.EmbeddedDriver";
    private final HikariConfig config;
    private final JdbcTemplate template;

    private static final String FIND_ALL_DB_OBJ = "";
    private static final String FIND_ALL_DEPENDENCY = "";
    private static final String FIND_SOURCE = "SELECT TEXT FROM DBA_SOURCE WHERE OWNER = ? AND NAME = ? ORDER BY LINE";

    public DerbyRepositoryImpl(String jdbcUrl, String username, String password) {
        config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setReadOnly(true);

        DataSource dataSource = new HikariDataSource(config);
        this.template = new JdbcTemplate(dataSource);
    }

    public DerbyRepositoryImpl(DerbyConnVO conn) {
        this(conn.getJdbcUrl(), conn.getUsername(), conn.getPassword());
    }

    public List<DbObj> findAllDbObj() {
        return template.query(FIND_ALL_DB_OBJ, (rs, rowNum) -> {
            return new DbObj(rs.getString(0), rs.getString(1), rs.getString(2));
        });
    }

    public List<Dependency> findAllDependency() {
        return template.query(FIND_ALL_DEPENDENCY, (rs, rowNum) -> {
            Dependency dep = new Dependency();
            return dep;
        });
    }

    public StringBuffer findSource(DbObj proc) {
        StringBuffer sb = new StringBuffer();

        template.query(FIND_SOURCE, (rs, rowNum) -> {
            sb.append(rs.getString(0));
            return null;
        }, proc.getOwner(), proc.getName());

        return sb;
    }

    public String getUsername() {
        return config.getUsername();
    }
}
