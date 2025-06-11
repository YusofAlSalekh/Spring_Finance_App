package ru.yusof.dao;

import org.springframework.stereotype.Service;
import ru.yusof.exceptions.AddingTransactionCategoryException;
import ru.yusof.exceptions.CreatingTransactionCategoryException;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.DeletionTransactionCategoryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Service
public class TransactionCategoryDao {
    private final DataSource dataSource;

    public TransactionCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionCategoryModel createTransactionCategory(String name, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into category (name,client_id) values (?,?)",
                    RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new CreatingTransactionCategoryException("Creating transaction category failed, no rows affected.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
                transactionCategoryModel.setId(resultSet.getInt(1));
                transactionCategoryModel.setName(name);
                transactionCategoryModel.setClientId(clientId);
                return transactionCategoryModel;
            } else {
                throw new CreatingTransactionCategoryException("Creating transaction type failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred during transaction type creation", e);
        }
    }

    public boolean deleteTransactionCategory(int transactionCategoryId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from category where id = ? and client_id = ?");
            preparedStatement.setInt(1, transactionCategoryId);
            preparedStatement.setInt(2, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DeletionTransactionCategoryException("No transaction type found with ID: " + transactionCategoryId);
            }
            return true;
        } catch (SQLException e) {
            throw new DaoException("Error occurred while deleting transaction type", e);
        }
    }

    public boolean editTransactionCategory(String newName, int transactionCategoryId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update category set name = ? where id = ? and client_id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, transactionCategoryId);
            preparedStatement.setInt(3, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new AddingTransactionCategoryException("No transaction type found with ID: " + transactionCategoryId);
            }
            return true;
        } catch (SQLException e) {
            throw new DaoException("Error occurred during transaction type editing", e);
        }
    }

    public List<TransactionCategoryModel> findByClientID(int clientId) {
        List<TransactionCategoryModel> transactionCategoryModels = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category where client_id = ?");
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TransactionCategoryModel transactionCategoryModel = new TransactionCategoryModel();
                transactionCategoryModel.setId(resultSet.getInt("id"));
                transactionCategoryModel.setName(resultSet.getString("name"));
                transactionCategoryModel.setClientId(resultSet.getInt("client_id"));
                transactionCategoryModels.add(transactionCategoryModel);
            }
            return transactionCategoryModels;
        } catch (SQLException e) {
            throw new DaoException("Database error occurred while fetching category by client ID.", e);
        }
    }
}
