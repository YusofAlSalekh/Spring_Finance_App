package ru.yusof.dao;

import org.springframework.stereotype.Service;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.CreatingAccountException;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.DeletionAccountException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Service
public class AccountDao {
    private final DataSource dataSource;

    public AccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<AccountModel> findByClientID(int clientId) {
        List<AccountModel> accountModels = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from account where client_id = ?");
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AccountModel accountModel = new AccountModel();
                accountModel.setId(resultSet.getInt("id"));
                accountModel.setName(resultSet.getString("name"));
                accountModel.setBalance(resultSet.getBigDecimal("balance"));
                accountModel.setClientId(resultSet.getInt("client_id"));
                accountModels.add(accountModel);
            }
            return accountModels;
        } catch (SQLException e) {
            throw new DaoException("Database error occurred while fetching accounts by client ID.", e);
        }
    }

    public AccountModel createAccount(String accountName, BigDecimal balance, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into account (name,balance,client_id) values (?,?,?)"
                            , RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, accountName);
            preparedStatement.setBigDecimal(2, balance);
            preparedStatement.setInt(3, clientId);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                AccountModel accountModel = new AccountModel();
                accountModel.setId(resultSet.getInt(1));
                accountModel.setName(accountName);
                accountModel.setBalance(balance);
                accountModel.setClientId(clientId);
                return accountModel;
            } else {
                throw new CreatingAccountException("Something went wrong during creating account. Please, try again later");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred during account creation", e);
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
                throw new DeletionAccountException("No account found with ID: " + accountId + " for client ID: " + clientId);
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred while deleting account", e);
        }
    }

    public AccountModel updateAccountName(String accountName, int accountId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement checkStatement = connection.prepareStatement("select 1 from account where name = ? and id <> ? and client_id = ? limit 1");
            checkStatement.setString(1, accountName);
            checkStatement.setInt(2, accountId);
            checkStatement.setInt(3, clientId);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                throw new AlreadyExistsException("The account with this name already exists");
            }

            PreparedStatement updateStatement =
                    connection.prepareStatement("update account set name = ? where id = ? and client_id = ?");
            updateStatement.setString(1, accountName);
            updateStatement.setInt(2, accountId);
            updateStatement.setInt(3, clientId);
            int resultSet = updateStatement.executeUpdate();

            if (resultSet == 1) {
                AccountModel accountModel = new AccountModel();
                accountModel.setId(accountId);
                accountModel.setName(accountName);
                accountModel.setClientId(clientId);
                return accountModel;
            } else {
                throw new CreatingAccountException("Something went wrong during updating account name. Please, try again later");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred during updating account name", e);
        }
    }
}