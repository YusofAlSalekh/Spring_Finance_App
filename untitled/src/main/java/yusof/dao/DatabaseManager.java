package yusof.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseManager {
    private static final DataSource dataSource = initializeDataSource();

    private static DataSource initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("060571");
        return new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
