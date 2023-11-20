package yusof;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.Scanner;

public class main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connection = connectToDatabase()) {
            while (true) {
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        register(connection);
                        break;
                    case 2:
                        login(connection);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login(Connection connection) {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        String passwordHex = DigestUtils.md5Hex(password);

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select id from client where email = ? and password = ?"
                )) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHex);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int clientId = resultSet.getInt("id");
                System.out.println("Login successfully");
                showDashboard(connection, clientId);
            } else {
                System.out.println("Login error. Check your password or email  ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Login error. Pleas try again");
        }
    }

    private static void showDashboard(Connection connection, int clientId) {
        while (true) {
            System.out.println("User Dashboard:");
            System.out.println("1. View Account Information");
            System.out.println("2. Create an Account");
            System.out.println("3. Delete an Account");
            System.out.println("4. Logout");

            int dashboardChoice = scanner.nextInt();
            scanner.nextLine();

            switch (dashboardChoice) {
                case 1:
                    viewAccountInformation(connection, clientId);
                    break;
                case 2:
                    creatAccount(connection, clientId);
                    break;
                case 3:
                    deleteAccount(connection, clientId);
                    break;
                case 4:
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    private static void deleteAccount(Connection connection, int clientId) {
        System.out.println("Enter account id to delete");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from account where id = ? and client_id = ?")
        ) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, clientId);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Account deleted successfully");
            } else {
                System.out.println("Failed to delete an account. Please check the account id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete the account. Please try again.");
        }
    }

    private static void creatAccount(Connection connection, int clientId) {
        System.out.println("Enter the Account name");
        String name = scanner.nextLine();
        System.out.println("Enter the Account balance");
        int balance = scanner.nextInt();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into account (name,balance,client_id) values (?,?,?)"
        )) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, balance);
            preparedStatement.setInt(3, clientId);

            preparedStatement.executeUpdate();
            System.out.println("Account created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Creating Account error!");
        }
    }

    private static void viewAccountInformation(Connection connection, int clientId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from account where client_id = ?"
        )) {
            preparedStatement.setInt(1, clientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ", " +
                        resultSet.getString("name") + ", " +
                        resultSet.getInt("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching account information.");
        }
    }

    private static void register(Connection connection) {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        String passwordHex = DigestUtils.md5Hex(password);

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into client (email,password) values (?,?)"
        )) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHex);

            preparedStatement.executeUpdate();

            System.out.println("Registration successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Registration error. Please try again");
        }
    }

    private static Connection connectToDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "060571"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
