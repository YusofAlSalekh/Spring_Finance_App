package yusof.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import yusof.exception.CustomException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private final DataSource dataSource;

    public AccountDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("060571");

        dataSource = new HikariDataSource(config);
    }

    public List<AccountModel> findByClientID(int clientId) {
        List<AccountModel> accountModels = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from account where client_id = ?");
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            AccountModel accountModel = null;
            while (resultSet.next()) {
                accountModel = new AccountModel();
                accountModel.setId(resultSet.getInt("id"));
                accountModel.setName(resultSet.getString("name"));
                accountModel.setBalance(resultSet.getInt("balance"));
                accountModel.setClient_id(resultSet.getInt("client_id"));
                accountModels.add(accountModel);
            }
            return accountModels;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public AccountModel createAccount(String accountName, int balance, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into account (name,balance,client_id) values (?,?,?)"
                            , Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, accountName);
            preparedStatement.setInt(2, balance);
            preparedStatement.setInt(3, clientId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                AccountModel accountModel = new AccountModel();
                accountModel.setId(resultSet.getInt(1));
                accountModel.setName(accountName);
                accountModel.setBalance(balance);
                accountModel.setClient_id(clientId);
                return accountModel;
            } else
                throw new CustomException("Can not generate id!");
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public void deleteAccount(int accountId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from account where id = ? and client_id = ?");
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, clientId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}
