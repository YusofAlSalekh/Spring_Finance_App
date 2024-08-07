package ru.yusof.view;

import ru.yusof.dao.CategoryAmountModel;
import ru.yusof.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        BigDecimal initialBalance = requestBigDecimal("Enter the initial balance:");

        accountService.createAccount(accountName, initialBalance, clientId);
        System.out.println("New account has been created successfully!");
    }

    private static void deleteAccount(AccountService accountService, int clientId) {
        int accountId = requestInt("Enter the account id:");
        accountService.deleteAccount(accountId, clientId);
        System.out.println("Account has been deleted successfully");
    }

    private static void deleteTransactionCategory(TransactionCategoryService transactionCategoryService, int clientId) {
        int transactionCategoryId = requestInt("Enter the Transaction Type id:");
        boolean result = transactionCategoryService.deleteTransactionCategory(transactionCategoryId, clientId);
        if (result) {
            System.out.println("Category has been deleted");
        }
    }

    private static void createTransactionCategory(TransactionCategoryService transactionCategoryService, int clientId) {
        String transactionName = requestString("Enter the category name:");
        transactionCategoryService.createCategory(transactionName, clientId);
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
            System.out.println("9. Transfer money between accounts");
            System.out.println("10. Logout");

            int dashboardChoice = requestInt("Enter your choice:");
            AccountService accountService = ServiceFactory.getAccountService();
            TransactionService transactionService = ServiceFactory.getTransactionService();
            TransactionCategoryService transactionCategoryService = ServiceFactory.getTransactionCategoryService();

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
                    createTransactionCategory(transactionCategoryService, clientId);
                    break;
                case 5:
                    deleteTransactionCategory(transactionCategoryService, clientId);
                    break;
                case 6:
                    editTransactionCategory(transactionCategoryService, clientId);
                    break;
                case 7:
                    getInformationByIncome(transactionService, clientId);
                    break;
                case 8:
                    getInformationByExpense(transactionService, clientId);
                    break;
                case 9:
                    performTransaction(transactionService, clientId);
                    break;
                case 10:
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    private static void performTransaction(TransactionService transactionService, int clientId) {
        int senderAccountId = requestInt("Enter the ID of the account you are going to send money from:");
        int receiverAccountId = requestInt("Enter the ID of the account you are going to send money to:");
        BigDecimal amount = requestBigDecimal("Enter the amount:");
        List<Integer> categoryIds = requestCategoryIds();

        transactionService.performTransaction(senderAccountId, receiverAccountId, amount, categoryIds);
        System.out.println("Transaction completed successfully!");
    }

    private static void editTransactionCategory(TransactionCategoryService transactionCategoryService, int clientId) {
        int accountId = requestInt("Enter the Transaction Type id:");
        String newName = requestString("Enter new Name for Transaction Type");
        boolean result = transactionCategoryService.editTransactionCategory(newName, accountId, clientId);
        if (result) {
            System.out.println("Transaction Type has been edited");
        }
    }

    private static void getInformationByIncome(TransactionService transactionService, int clientId) {
        LocalDate start = requestLocalDate("Enter start date");
        LocalDate end = requestLocalDate("Enter end date");

        System.out.println("Here is information about your income:");

        List<CategoryAmountModel> categoryAmountModels = transactionService.getIncomeReportByCategory(clientId, start, end);

        if (!categoryAmountModels.isEmpty()) {
            for (CategoryAmountModel categoryAmountModel : categoryAmountModels) {
                System.out.println(categoryAmountModel);
            }
        } else {
            System.out.println("No income information found for the client.");
        }
    }

    private static void getInformationByExpense(TransactionService transactionService, int clientId) {
        LocalDate start = requestLocalDate("Enter start date");
        LocalDate end = requestLocalDate("Enter end date");

        System.out.println("Here is information about your expenses:");

        List<CategoryAmountModel> categoryAmountModels = transactionService.getExpenseReportByCategory(clientId, start, end);

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

    static BigDecimal requestBigDecimal(String title) {
        System.out.println(title);
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        BigDecimal number = scanner.nextBigDecimal();
        scanner.nextLine();
        return number;
    }

    private static List<Integer> requestCategoryIds() {
        System.out.println("Enter the category IDs (comma separated):");
        String input = scanner.nextLine();
        String[] parts = input.split(",");
        List<Integer> categoryIds = new ArrayList<>();
        for (String part : parts) {
            try {
                categoryIds.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for category ID: " + part);
            }
        }
        return categoryIds;
    }
}