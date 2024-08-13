package ru.yusof.dao;

import org.springframework.stereotype.Service;
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

@Service
public class TransactionDao {
    private final DataSource dataSource;

    public TransactionDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
                categoryAmountModel.setCategoryName(resultSet.getString("category_name"));
                categoryAmountModel.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                categoryAmountModels.add(categoryAmountModel);
            }
            if (!found) {
                throw new GetExpenseByCategoryException("Something went wrong during fetching exception.");
            }
            return categoryAmountModels;
        } catch (SQLException e) {
            throw new DaoException("Database error during expense data fetch", e);
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                CategoryAmountModel categoryAmountModel = new CategoryAmountModel();
                categoryAmountModel.setCategoryName(resultSet.getString("category_name"));
                categoryAmountModel.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                categoryAmountModels.add(categoryAmountModel);
            }
            if (!found) {
                throw new GetIncomeByCategoryException("Something went wrong during fetching exception.");
            }
            return categoryAmountModels;
        } catch (SQLException e) {
            throw new DaoException("Database error during income data fetch", e);
        }
    }

    public void addTransaction(int senderAccountId, int receiverAccountId, int clientId, BigDecimal amount, List<Integer> categoryIds) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            isSenderHasSufficientBalance(senderAccountId, clientId, amount, connection);

            subtractMoneyFromSender(senderAccountId, clientId, amount, connection);

            addMoneyToReceiver(receiverAccountId, amount, connection);

            PreparedStatement insertTransactionStatement = getInsertTransactionStatement(senderAccountId, receiverAccountId, amount, connection);

            insertCategoriesIntoTransactionToCategoryTable(categoryIds, insertTransactionStatement, connection);
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new DaoException("Failed to rollback transaction.", rollbackException);
                }
            }
            throw new CustomException("Error occurred during transaction.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    throw new DaoException("Failed to close connection.", closeException);
                }
            }
        }
    }

    private static void insertCategoriesIntoTransactionToCategoryTable(List<Integer> categoryIds, PreparedStatement insertTransactionStatement, Connection connection) {
        int transactionId = getGeneratedTransactionId(insertTransactionStatement);
        try {
            for (int categoryId : categoryIds) {
                PreparedStatement insertCategoryStatement = connection.prepareStatement(
                        "insert into transaction_to_category (transaction_id, category_id) values (?,?)"
                );
                insertCategoryStatement.setInt(1, transactionId);
                insertCategoryStatement.setInt(2, categoryId);
                insertCategoryStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred while inserting data to TransactionToCategoryTable", e);
        }
    }

    private static PreparedStatement getInsertTransactionStatement(int senderAccountId, int receiverAccountId, BigDecimal amount, Connection connection) {
        int affectedRows;
        try {
            PreparedStatement insertTransactionStatement = connection.prepareStatement(
                    "insert into transaction (created_date, amount, sender_account_id, receiver_account_id) values (CURRENT_TIMESTAMP, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );
            insertTransactionStatement.setBigDecimal(1, amount);
            insertTransactionStatement.setInt(2, senderAccountId);
            insertTransactionStatement.setInt(3, receiverAccountId);
            affectedRows = insertTransactionStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new DaoException("Failed to insert transaction record.");
            }
            return insertTransactionStatement;
        } catch (SQLException e) {
            throw new DaoException("Error occurred while inserting transaction", e);
        }
    }

    private static int getGeneratedTransactionId(PreparedStatement insertTransactionStatement) {
        try {
            ResultSet generatedKey = insertTransactionStatement.getGeneratedKeys();
            int transactionId;
            if (generatedKey.next()) {
                transactionId = generatedKey.getInt(1);
            } else {
                throw new DaoException("Failed to obtain transaction ID.");
            }
            return transactionId;
        } catch (SQLException e) {
            throw new DaoException("Error occurred while retrieving transaction ID", e);
        }
    }

    private void addMoneyToReceiver(int receiverAccountId, BigDecimal amount, Connection connection) {
        try {
            PreparedStatement addToReceiverBalanceStatement = connection.prepareStatement(
                    "update account set balance = balance + ? where id = ?"
            );
            addToReceiverBalanceStatement.setBigDecimal(1, amount);
            addToReceiverBalanceStatement.setInt(2, receiverAccountId);
            int affectedRows = addToReceiverBalanceStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new AddingTheAmountException("Failed to update receiver's account balance.");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred while adding money to receiver's account", e);
        }
    }

    private void subtractMoneyFromSender(int senderAccountId, int clientId, BigDecimal amount, Connection connection) {
        try {
            PreparedStatement subtractFromSenderBalanceStatement = connection.prepareStatement(
                    "update account set balance = balance - ?  where id = ? and client_id = ?"
            );
            subtractFromSenderBalanceStatement.setBigDecimal(1, amount);
            subtractFromSenderBalanceStatement.setInt(2, senderAccountId);
            subtractFromSenderBalanceStatement.setInt(3, clientId);

            int affectedRows = subtractFromSenderBalanceStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new SubtractingTheAmountException("Failed to update sender's account balance.");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred while updating sender's account balance", e);
        }
    }

    private void isSenderHasSufficientBalance(int senderAccountId, int clientId, BigDecimal amount, Connection connection) {
        try {
            PreparedStatement checkBalanceStatement = connection.prepareStatement(
                    "select balance from account where id = ? and client_id = ?"
            );
            checkBalanceStatement.setInt(1, senderAccountId);
            checkBalanceStatement.setInt(2, clientId);
            ResultSet balanceResultSet = checkBalanceStatement.executeQuery();

            if (!balanceResultSet.next()) {
                throw new IllegalOwnerException("Account does not belong to the user.");
            }

            if (balanceResultSet.getBigDecimal("balance").compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in sender's account.");
            }
        } catch (SQLException e) {
            throw new DaoException("Error occurred while checking sender's account balance", e);
        }
    }
}