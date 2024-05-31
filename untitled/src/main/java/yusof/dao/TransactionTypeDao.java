package yusof.dao;

import yusof.exceptions.CustomException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));

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
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));

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

