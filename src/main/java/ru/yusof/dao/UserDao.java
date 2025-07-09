package ru.yusof.dao;

import org.springframework.stereotype.Service;
import ru.yusof.exceptions.AlreadyExistsException;
import ru.yusof.exceptions.DaoException;
import ru.yusof.exceptions.RegistrationOfANewUserException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Service
public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
                userModel = createUserModelByResultSet(resultSet);
            }
            return Optional.ofNullable(userModel);
        } catch (SQLException e) {
            throw new DaoException("Error occurred during finding by email and hash", e);
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
            throw new DaoException("Error occurred during inserting new user", e);
        }
    }

    public Optional<UserModel> findById(Integer userId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from client where id = ?");
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            UserModel userModel = null;
            if (resultSet.next()) {
                userModel = createUserModelByResultSet(resultSet);
            }
            return Optional.ofNullable(userModel);
        } catch (SQLException e) {
            throw new DaoException("Error occurred during finding by email and hash", e);
        }
    }

    private static UserModel createUserModelByResultSet(ResultSet resultSet) throws SQLException {
        UserModel userModel = new UserModel();
        userModel.setId(resultSet.getInt("id"));
        userModel.setEmail(resultSet.getString("email"));
        userModel.setPassword(resultSet.getString("password"));
        return userModel;
    }
}