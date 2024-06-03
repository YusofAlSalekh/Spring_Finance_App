package ru.yusof.dao;

import ru.yusof.exceptions.CustomException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AccountDao {
    private final DataSource dataSource;

    public AccountDao() {
        this.dataSource = DatabaseManager.getDataSource();
    }

    public List<AccountModel> findByClientID(int clientId) {
        List<AccountModel> accountModels = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from account where client_id = ?");
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean found = false;
            while (resultSet.next()) {
                found = true;
                AccountModel accountModel = new AccountModel();
                accountModel.setId(resultSet.getInt("id"));
                accountModel.setName(resultSet.getString("name"));
                accountModel.setBalance(resultSet.getInt("balance"));
                accountModel.setClient_id(resultSet.getInt("client_id"));
                accountModels.add(accountModel);
            }
            if (!found) {
                throw new NoSuchElementException("No accounts found for client ID: " + clientId);
            }
            return accountModels;
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while fetching accounts by client ID.", e);
        }
    }

    public AccountModel createAccount(String accountName, double balance, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into account (name,balance,client_id) values (?,?,?)"
                            , RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, accountName);
            preparedStatement.setDouble(2, balance);
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
            } else {
                throw new CustomException("Something went wrong during creating account. Please, try again later");
            }
        } catch (SQLException e) {
            throw new CustomException("Error occurred during account creation", e);
        }
    }

    public void deleteAccount(int accountId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from account where id = ? and client_id = ?");
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalStateException("No account found with ID: " + accountId + " for client ID: " + clientId);
            }
        } catch (SQLException e) {
            throw new CustomException("Error occurred while deleting account", e);
        }
    }
}