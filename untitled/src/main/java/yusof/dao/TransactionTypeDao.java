package yusof.dao;

import yusof.exceptions.CustomException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TransactionTypeDao {
    private final DataSource dataSource;

    public TransactionTypeDao() {
        this.dataSource = DatabaseManager.getDataSource();
    }

    public TransactionTypeModel createTransactionType(String name, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into category (name,client_id) values (?,?)",
                    RETURN_GENERATED_KEYS);
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
            } else {
                throw new CustomException("Something went wrong during generating new category. Please, try again later!");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public boolean deleteTransactionType(int transactionTypeId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from category where id = ? and client_id = ?");
            preparedStatement.setInt(1, transactionTypeId);
            preparedStatement.setInt(2, clientId);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                throw new CustomException("Something went wrong. Please, try to delete again later!");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public boolean editTransactionType(String newName, int transactionTypeId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update category set name = ? where id = ? and client_id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, transactionTypeId);
            preparedStatement.setInt(3, clientId);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            } else {
                throw new CustomException("Something went wrong during editing. Please, try again later!");
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}

