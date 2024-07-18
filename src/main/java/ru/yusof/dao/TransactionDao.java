package ru.yusof.dao;

import ru.yusof.exceptions.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TransactionDao {
    private final DataSource dataSource;

    public TransactionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionTypeModel createTransactionType(String name, int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            if (!clientIdExists(clientId)) {
                throw new CreatingTransactionTypeException("Client ID does not exist: " + clientId);
            }

            // Insert new transaction type
            PreparedStatement preparedStatement = connection.prepareStatement("insert into category (name,client_id) values (?,?)",
                    RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, clientId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new CreatingTransactionTypeException("Creating transaction type failed, no rows affected.");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setId(resultSet.getInt(1));
                transactionTypeModel.setName(name);
                transactionTypeModel.setClientId(clientId);
                return transactionTypeModel;
            } else {
                throw new CreatingTransactionTypeException("Creating transaction type failed, no ID obtained.");
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
                throw new DeletionTransactionTypeException("No transaction type found with ID: " + transactionTypeId);
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
                throw new AddingTransactionTypeException("No transaction type found with ID: " + transactionTypeId);
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

        try (Connection connection = dataSource.getConnection()) {
            if (!clientIdExists(clientId)) {
                throw new GetExpenseByCategoryException("Client ID does not exist: " + clientId);
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            return categoryAmountModels;
        } catch (SQLException e) {
            throw new RuntimeException("Database error during expense data fetch", e);
        }
    }

    public boolean clientIdExists(int clientId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement checkClientStatement = connection.prepareStatement("SELECT * FROM client WHERE id = ?");
            checkClientStatement.setInt(1, clientId);
            ResultSet clientResultSet = checkClientStatement.executeQuery();
            return clientResultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        try (Connection connection = dataSource.getConnection()) {
            if (!clientIdExists(clientId)) {
                throw new GetIncomeByCategoryException("Client ID does not exist: " + clientId);
            }

            // Fetch income by category
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
            return categoryAmountModels;
        } catch (SQLException e) {
            throw new RuntimeException("Database error during income data fetch", e);
        }
    }

    public void addTransaction(int senderAccountId, int receiverAccountId, BigDecimal amount, List<Integer> categoryIds) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            // Check if sender account has sufficient balance
            PreparedStatement checkBalanceStatement = connection.prepareStatement(
                    "select balance from account where id = ?"
            );
            checkBalanceStatement.setInt(1, senderAccountId);
            ResultSet balanceResultSet = checkBalanceStatement.executeQuery();
            if (!balanceResultSet.next() || balanceResultSet.getBigDecimal("balance").compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in sender's account.");
            }

            // Subtract amount from sender's account
            PreparedStatement subtractFromSenderBalanceStatement = connection.prepareStatement(
                    "update account set balance = balance - ? where id = ?"
            );
            subtractFromSenderBalanceStatement.setBigDecimal(1, amount);
            subtractFromSenderBalanceStatement.setInt(2, senderAccountId);

            int affectedRows = subtractFromSenderBalanceStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new SubtractingTheAmountException("Failed to update sender's account balance.");
            }

            // Add amount to receiver's account
            PreparedStatement addToReceiverBalanceStatement = connection.prepareStatement(
                    "update account set balance = balance + ? where id = ?"
            );
            addToReceiverBalanceStatement.setBigDecimal(1, amount);
            addToReceiverBalanceStatement.setInt(2, receiverAccountId);
            affectedRows = addToReceiverBalanceStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new AddingTheAmountException("Failed to update receiver's account balance.");
            }

            // Insert transaction record
            PreparedStatement insertTransactionStatement = connection.prepareStatement(
                    "insert into transaction (created_date, amount, sender_account_id, receiver_account_id) values (CURRENT_TIMESTAMP, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );
            insertTransactionStatement.setBigDecimal(1, amount);
            insertTransactionStatement.setInt(2, senderAccountId);
            insertTransactionStatement.setInt(3, receiverAccountId);
            affectedRows = insertTransactionStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Failed to insert transaction record.");
            }

            // Get the generated transaction ID
            ResultSet generatedKey = insertTransactionStatement.getGeneratedKeys();
            int transactionId;
            if (generatedKey.next()) {
                transactionId = generatedKey.getInt(1);
            } else {
                throw new SQLException("Failed to obtain transaction ID.");
            }

            // Insert related categories into transaction_to_category
            for (int categoryId : categoryIds) {
                PreparedStatement insertCategoryStatement = connection.prepareStatement(
                        "insert into transaction_to_category (transaction_id, category_id) values (?,?)"
                );
                insertCategoryStatement.setInt(1, transactionId);
                insertCategoryStatement.setInt(2, categoryId);
                insertCategoryStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new CustomException("Failed to rollback transaction.", rollbackException);
                }
            }
            throw new CustomException("Error occurred during transaction.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    throw new CustomException("Failed to close connection.", closeException);
                }
            }
        }
    }
}