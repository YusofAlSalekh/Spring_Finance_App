package ru.yusof.view;

import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.service.*;
import ru.yusof.service.AccountDTO;
import ru.yusof.service.AccountService;
import ru.yusof.service.AuthorizationService;
import ru.yusof.service.TransactionTypeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        AuthorizationService authorizationService = ServiceFactory.getAuthorizationService();

        while (true) {
            try {
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");

                int choice = requestInt("Enter your choice:");

                switch (choice) {
                    case 1:
                        register(authorizationService);
                        break;
                    case 2:
                        login(authorizationService);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    private static void register(AuthorizationService authorizationService) {
        String email = requestString("Enter your email:");
        String password = requestString("Enter your password:");
        authorizationService.register(email, password);
    }

    private static void login(AuthorizationService authorizationService) {
        String email = requestString("Enter your email:");
        String password = requestString("Enter your password:");

        authorizationService.authorize(email, password);
        int clientId = authorizationService.getClientId();
        showDashboard(clientId);
    }

    private static void viewAccountInformation(AccountService accountService, int clientId) {
        List<AccountDTO> accountDTOs = accountService.viewAccount(clientId);

        if (!accountDTOs.isEmpty()) {
            for (AccountDTO accountDTO : accountDTOs) {
                System.out.println(accountDTO);
            }
        } else {
            System.out.println("No accounts found for the client.");
        }
    }

    private static void createNewAccount(AccountService accountService, int clientId) {
        String accountName = requestString("Enter the account name:");
        double initialBalance = requestDouble("Enter the initial balance:");

        accountService.createAccount(accountName, initialBalance, clientId);
        System.out.println("New account has been created successfully!");
    }

    private static void deleteAccount(AccountService accountService, int clientId) {
        int accountId = requestInt("Enter the account id:");
        accountService.deleteAccount(accountId, clientId);
        System.out.println("Account has been deleted successfully");
    }

    private static void deleteTransactionType(TransactionTypeService transactionTypeService, int clientId) {
        int transactionTypeId = requestInt("Enter the Transaction Type id:");
        boolean result = transactionTypeService.deleteTransactionType(transactionTypeId, clientId);
        if (result) {
            System.out.println("Category has been deleted");
        }
    }

    private static void createTransactionType(TransactionTypeService transactionTypeService, int clientId) {
        String transactionName = requestString("Enter the category name:");
        transactionTypeService.createCategory(transactionName, clientId);
        System.out.println("New category created successfully!");
    }

    private static void showDashboard(int clientId) {
        while (true) {
            System.out.println("User Dashboard:");
            System.out.println("1. View Account Information");
            System.out.println("2. Create an Account");
            System.out.println("3. Delete an Account");
            System.out.println("4. Create transaction type");
            System.out.println("5. Delete transaction type");
            System.out.println("6. Edit transaction type");
            System.out.println("7. Get income information");
            System.out.println("8. Get expense information");
            System.out.println("9. Logout");

            int dashboardChoice = requestInt("Enter your choice:");
            AccountService accountService = ServiceFactory.getAccountService();
            TransactionTypeService transactionTypeService = ServiceFactory.getTransactionTypeService();

            switch (dashboardChoice) {
                case 1:
                    viewAccountInformation(accountService, clientId);
                    break;
                case 2:
                    createNewAccount(accountService, clientId);
                    break;
                case 3:
                    deleteAccount(accountService, clientId);
                    break;
                case 4:
                    createTransactionType(transactionTypeService, clientId);
                    break;
                case 5:
                    deleteTransactionType(transactionTypeService, clientId);
                    break;
                case 6:
                    editTransactionType(transactionTypeService, clientId);
                    break;
                case 7:
                    getInformationByIncome(transactionTypeService, clientId);
                    break;
                case 8:
                    getInformationByExpense(transactionTypeService, clientId);
                    break;
                case 9:
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    private static void editTransactionType(TransactionTypeService transactionTypeService, int clientId) {
        int accountId = requestInt("Enter the Transaction Type id:");
        String newName = requestString("Enter new Name for Transaction Type");
        boolean result = transactionTypeService.editTransactionType(newName, accountId, clientId);
        if (result) {
            System.out.println("Transaction Type has been edited");
        }
    }

    private static void getInformationByIncome(TransactionTypeService transactionTypeService, int clientId) {
        LocalDate start = requestLocalDate("Enter start date");
        LocalDate end = requestLocalDate("Enter end date");

        System.out.println("Here is information about your income:");

        List<CategoryAmountModel> categoryAmountModels = transactionTypeService.getIncomeReportByCategory(clientId, start, end);

        if (!categoryAmountModels.isEmpty()) {
            for (CategoryAmountModel categoryAmountModel : categoryAmountModels) {
                System.out.println(categoryAmountModel);
            }
        } else {
            System.out.println("No income information found for the client.");
        }
    }

    private static void getInformationByExpense(TransactionTypeService transactionTypeService, int clientId) {
        LocalDate start = requestLocalDate("Enter start date");
        LocalDate end = requestLocalDate("Enter end date");

        System.out.println("Here is information about your expenses:");

        List<CategoryAmountModel> categoryAmountModels = transactionTypeService.getExpenseReportByCategory(clientId, start, end);

        if (!categoryAmountModels.isEmpty()) {
            for (CategoryAmountModel categoryAmountModel : categoryAmountModels) {
                System.out.println(categoryAmountModel);
            }
        } else {
            System.out.println("No expense information found for the client.");
        }
    }

    static String requestString(String title) {
        System.out.println(title);
        return scanner.next();
    }

    static int requestInt(String title) {
        System.out.println(title);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }

    static double requestDouble(String title) {
        System.out.println(title);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        double number = scanner.nextDouble();
        scanner.nextLine();
        return number;
    }

    public static LocalDate requestLocalDate(String prompt) {
        System.out.println(prompt + " (YYYY-MM-DD):");
        while (true) {
            String dateInput = scanner.nextLine().trim();
            if (!dateInput.isEmpty()) {
                try {
                    return LocalDate.parse(dateInput, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD (e.g., 2024-05-26).");
                }
            } else {
                System.out.println("No input detected. Please enter a date.");
            }
        }
    }
}