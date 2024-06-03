package ru.yusof.dao;

import ru.yusof.exceptions.CustomException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction type failed, no rows affected.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setId(resultSet.getInt(1));
                transactionTypeModel.setName(name);
                transactionTypeModel.setClientId(clientId);
                return transactionTypeModel;
            } else {
                throw new SQLException("Creating transaction type failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new CustomException("Error occurred during transaction type creation", e);
        }
    }

    public boolean deleteTransactionType(int transactionTypeId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from category where id = ? and client_id = ?");
            preparedStatement.setInt(1, transactionTypeId);
            preparedStatement.setInt(2, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchElementException("No transaction type found with ID: " + transactionTypeId);
            }
            return true;
        } catch (SQLException e) {
            throw new CustomException("Error occurred while deleting transaction type", e);
        }
    }

    public boolean editTransactionType(String newName, int transactionTypeId, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update category set name = ? where id = ? and client_id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, transactionTypeId);
            preparedStatement.setInt(3, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchElementException("No transaction type found with ID: " + transactionTypeId);
            }
            return true;
        } catch (SQLException e) {
            throw new CustomException("Error occurred during transaction type editing", e);
        }
    }

    public List<CategoryAmountModel> fetchExpenseByCategory(int clientId, LocalDate startDate, LocalDate endDate) {
        List<CategoryAmountModel> categoryAmountModels = new ArrayList<>();
        String sql =
                "SELECT cat.name AS category_name, SUM(trans.amount) AS total_amount " +
                        "FROM transaction trans " +
                        "JOIN transaction_to_category ttc ON ttc.transaction_id = trans.id " +
                        "JOIN category cat ON cat.id = ttc.category_id " +
                        "JOIN account acc ON acc.id = trans.sender_account_id " +
                        "WHERE acc.client_id = ? AND trans.created_date BETWEEN ? AND ? " +
                        "GROUP BY cat.name;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, clientId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
                categoryAmountModel.setCategoryName(resultSet.getString("category_name"));
                categoryAmountModel.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                categoryAmountModels.add(categoryAmountModel);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during expense data fetch", e);
        }
        return categoryAmountModels;
    }

    public List<CategoryAmountModel> fetchIncomeByCategory(int clientId, LocalDate startDate, LocalDate endDate) {
        List<CategoryAmountModel> categoryAmountModels = new ArrayList<>();
        String sql =
                "SELECT cat.name AS category_name, SUM(trans.amount) AS total_amount " +
                        "FROM transaction trans " +
                        "JOIN transaction_to_category ttc ON trans.id = ttc.transaction_id " +
                        "JOIN category cat ON ttc.category_id = cat.id " +
                        "JOIN account acc ON trans.receiver_account_id = acc.id " +
                        "WHERE acc.client_id = ? AND trans.created_date BETWEEN ? AND ? " +
                        "GROUP BY cat.name;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, clientId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
                categoryAmountModel.setCategoryName(resultSet.getString("category_name"));
                categoryAmountModel.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                categoryAmountModels.add(categoryAmountModel);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during income data fetch", e);
        }
        return categoryAmountModels;
    }
}