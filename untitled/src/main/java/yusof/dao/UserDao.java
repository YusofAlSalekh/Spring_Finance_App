package yusof.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import yusof.exception.CustomException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private final DataSource dataSource;

    public UserDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("060571");

        dataSource = new HikariDataSource(config);
    }

    public UserModel findByEmailAndHash(String email, String hash) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from client where email=? " +
                            "and password = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hash);

            ResultSet resultSet = preparedStatement.executeQuery();

            UserModel userModel = null;
            if (resultSet.next()) {
                userModel = new UserModel();
                userModel.setId(resultSet.getInt("id"));
                userModel.setEmail(resultSet.getString("email"));
                userModel.setPassword(resultSet.getString("password"));
            }
            return userModel;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public UserModel insert(String email, String hash) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into client (email,password) values (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hash);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                UserModel userModel = new UserModel();
                userModel.setId(resultSet.getInt(1));
                userModel.setEmail(email);
                userModel.setPassword(hash);
                return userModel;
            } else
                throw new CustomException("Can not generate id!");
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}
