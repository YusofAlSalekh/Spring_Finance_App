package yusof.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import yusof.exception.CustomException;

import javax.sql.DataSource;
import java.sql.*;

public class TransactionTypeDao {
    private final DataSource dataSource;

    public TransactionTypeDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("060571");

        dataSource = new HikariDataSource(config);
    }

    public TransactionTypeModel createTransactionType(String name, int clientId) {
        try (
                Connection connection = dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("insert into category (name,client_id) values (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, clientId);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setId(resultSet.getInt(1));
                transactionTypeModel.setName(name);
                transactionTypeModel.setClientId(clientId);
                return transactionTypeModel;
            } else
                throw new CustomException("Can not generate new category!");
        } catch (
                SQLException e) {
            throw new CustomException(e);
        }
    }
}
