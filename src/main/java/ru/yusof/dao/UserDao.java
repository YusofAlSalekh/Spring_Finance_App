package ru.yusof.dao;

import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.CustomException;
import ru.yusof.exceptions.RegistrationOfANewUserException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDao {
    private final DataSource dataSource;

    public UserDao() {
        this.dataSource = DatabaseManager.getDataSource();
    }

    public Optional<UserModel> findByEmailAndHash(String email, String hash) {
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
            return Optional.ofNullable(userModel);
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    public UserModel insert(String email, String hash) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into client (email,password) values (?,?)",
                    RETURN_GENERATED_KEYS);
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
            } else {
                throw new RegistrationOfANewUserException("Something went wrong during registration. Please, try again later");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException("User with the given email already exists.");
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }
}