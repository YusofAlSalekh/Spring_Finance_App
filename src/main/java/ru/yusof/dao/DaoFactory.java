package ru.yusof.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DaoFactory {
    private static UserDao userDao;
    private static AccountDao accountDao;
    private static TransactionDao transactionDao;
    private static DataSource dataSource;

    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao(getDataSource());
        }
        return userDao;
    }

    public static AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDao(getDataSource());
        }
        return accountDao;
    }

    public static TransactionDao getTransactionTypeDao() {
        if (transactionDao == null) {
            transactionDao = new TransactionDao(getDataSource());
        }
        return transactionDao;
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getProperty("jdbcUrl", "jdbc:postgresql://localhost:5432/postgres"));
            config.setUsername(System.getProperty("jdbcUser", "postgres"));
            config.setPassword(System.getProperty("jdbcPassword", "060571"));
            dataSource = new HikariDataSource(config);

            initDataBase();
        }
        return dataSource;
    }

    private static void initDataBase() {
        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            Liquibase liquibase = new Liquibase(
                    System.getProperty("liquibaseFile", "liquibase.xml"),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new Contexts());
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
    public static void resetDataSource() {
        dataSource = null;
    }

    public static void resetAccountDao() {
        accountDao = null;
    }

    public static void resetTransactionDao() {
        transactionDao = null;
    }

    public static void resetUserDao() {
        userDao = null;
    }
}