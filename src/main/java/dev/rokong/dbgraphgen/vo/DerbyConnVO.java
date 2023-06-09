package dev.rokong.dbgraphgen.vo;

public class DerbyConnVO {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DerbyConnVO(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
