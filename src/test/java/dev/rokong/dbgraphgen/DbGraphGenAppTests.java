package dev.rokong.dbgraphgen;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.rokong.dbgraphgen.entity.DbObj;
import dev.rokong.dbgraphgen.entity.Dependency;
import dev.rokong.dbgraphgen.repository.DbObjRepository;
import dev.rokong.dbgraphgen.repository.DependencyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DbGraphGenAppTests {

	private static final Logger log = LoggerFactory.getLogger(DbGraphGenAppTests.class);

	@Autowired private EntityManager em;
	@Autowired private DbObjRepository dbObjRepo;
	@Autowired private DependencyRepository depRepo;

	@Test @Disabled
	public void manualDataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		config.setJdbcUrl("jdbc:derby:directory:~/derby;create=true");
		config.setUsername("sa");
		config.setPassword("");

		DataSource derbyDataSource = new HikariDataSource(config);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(derbyDataSource);

		List<String> rows;
		rows = jdbcTemplate.queryForList("SELECT 'HELLO_WORLD' FROM SYSIBM.SYSDUMMY1", String.class);
		rows = jdbcTemplate.queryForList("SELECT 'HELLO_WORLD' FROM SYSIBM.SYSDUMMY1", String.class);
		rows = jdbcTemplate.queryForList("SELECT 'HELLO_WORLD' FROM SYSIBM.SYSDUMMY1", String.class);
		rows.forEach(log::info);
	}

	@Test
	public void dbObjectTest() {
		dbObjRepo.findAll().forEach(dbObj -> {
			log.info(String.format("%s", dbObj.getName()));
		});
	}

	@Test
	public void dependencyTest() {
		depRepo.findAll().forEach(dep -> {
			log.info(String.format("%s <- %s", dep.getObj().getName(), dep.getRef().getName()));
		});
	}

	@Test
	@Transactional
	public void depInsertTest() {
		DbObj obj = dbObjRepo.findByType("PROCEDURE").get(0);
		DbObj ref = new DbObj("TABLE", "U1", "T4");
		dbObjRepo.saveAndFlush(ref);

		long prevCnt = depRepo.count();

		Dependency dep = new Dependency(obj, ref);
		depRepo.saveAndFlush(dep);

		assertEquals(depRepo.count(), prevCnt + 1);
	}

	@Test
	@Transactional
	public void depInsertKeyTest() {
		DbObj proc = dbObjRepo.findByType("PROCEDURE").get(0);
		DbObj tab = dbObjRepo.findByType("TABLE").get(0);

		Dependency dep = new Dependency(em.getReference(DbObj.class, proc.getId()), em.getReference(DbObj.class, tab.getId()));
		depRepo.saveAndFlush(dep);

		Optional<Dependency> optDep = depRepo.findById(dep.getId());
		if (optDep.isPresent()) {
			dep = optDep.get();
			log.info(String.format("%s, %s, %s", dep, dep.getObj().getName(), dep.getRef().getName()));
		}
	}
}
