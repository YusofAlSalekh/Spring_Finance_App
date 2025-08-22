package com.yusof.web.console;

import com.yusof.web.api.controller.TransactionCommandCreation;
import com.yusof.web.entity.CategoryAmountModel;
import com.yusof.web.exceptions.*;
import com.yusof.web.service.*;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final TransactionCategoryService transactionCategoryService;
    private final AuthorizationService authorizationService;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            try {
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");

                int choice = requestInt("Enter your choice:");

                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            } catch (AlreadyExistsException | BadCredentialsException | UnauthorizedException | NotFoundException |
                     IllegalOwnerException | OperationFailedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (ConstraintViolationException e) {
                e.getConstraintViolations().forEach(v -> System.out.println(v.getMessage()));
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    private void register() {
        String email = requestString("Enter your email:");
        String password = requestString("Enter your password:");

        authorizationService.register(email, password);
        System.out.println("Your bank account has been created.");
    }

    private void login() {
        String email = requestString("Enter your email:");
        String password = requestString("Enter your password:");

        authorizationService.authorize(email, password);
        int clientId = authorizationService.getClientId();
        System.out.println("You are logged in as " + email + ".");
        showDashboard(clientId);
    }

    private void showDashboard(int clientId) {
        while (true) {
            System.out.println("User Dashboard:");
            System.out.println("1. Create an Account");
            System.out.println("2. Show Accounts");
            System.out.println("3. Delete an Account");
            System.out.println("4. Change Account Name");
            System.out.println("5. Create Category");
            System.out.println("6. Show Categories");
            System.out.println("7. Delete Category");
            System.out.println("8. Change Category Name");
            System.out.println("9. Show income information");
            System.out.println("10. Show expense information");
            System.out.println("11. Transfer money between accounts");
            System.out.println("12. Logout");

            int dashboardChoice = requestInt("Enter your choice:");

            switch (dashboardChoice) {
                case 1:
                    createNewAccount(clientId);
                    break;
                case 2:
                    showAccounts(clientId);
                    break;
                case 3:
                    deleteAccount(clientId);
                    break;
                case 4:
                    changeAccountName(clientId);
                case 5:
                    createTransactionCategory(clientId);
                    break;
                case 6:
                    showTransactionCategories(clientId);
                    break;
                case 7:
                    deleteTransactionCategory(clientId);
                    break;
                case 8:
                    updateTransactionCategory(clientId);
                    break;
                case 9:
                    getInformationByIncome(clientId);
                    break;
                case 10:
                    getInformationByExpense(clientId);
                    break;
                case 11:
                    performTransaction(clientId);
                    break;
                case 12:
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    private void showTransactionCategories(int clientId) {
        List<TransactionCategoryDTO> transactionCategoryDTOs = transactionCategoryService.viewTransactionCategory(clientId);
        if (!transactionCategoryDTOs.isEmpty()) {
            for (TransactionCategoryDTO transactionCategoryDTO : transactionCategoryDTOs) {
                System.out.println(transactionCategoryDTO);
            }
        } else {
            System.out.println("No categories found for the client.");
        }
    }

    private void changeAccountName(int clientId) {
        int accountId = requestInt("Enter the Account id:");
        String newName = requestString("Enter new Name for Account");

        accountService.updateAccountName(newName, accountId, clientId);
        System.out.println("Account name has been changed to " + newName + ".");
    }

    private void showAccounts(int clientId) {
        List<AccountDTO> accountDTOs = accountService.viewAccount(clientId);
        if (!accountDTOs.isEmpty()) {
            for (AccountDTO accountDTO : accountDTOs) {
                System.out.println(accountDTO);
            }
        } else {
            System.out.println("No accounts found for the client.");
        }
    }

    private void createNewAccount(int clientId) {
        String accountName = requestString("Enter the account name:");
        BigDecimal initialBalance = requestBigDecimal("Enter the initial balance:");

        accountService.createAccount(accountName, initialBalance, clientId);
        System.out.println("New account has been created successfully!");
    }

    private void deleteAccount(int clientId) {
        int accountId = requestInt("Enter the account id:");

        accountService.deleteAccount(accountId, clientId);
        System.out.println("Account has been deleted successfully");
    }

    private void deleteTransactionCategory(int clientId) {
        int transactionCategoryId = requestInt("Enter the Transaction Type id:");

        transactionCategoryService.deleteTransactionCategory(transactionCategoryId, clientId);
        System.out.println("Category has been deleted");
    }

    private void createTransactionCategory(int clientId) {
        String transactionName = requestString("Enter the category name:");

        transactionCategoryService.createCategory(transactionName, clientId);
        System.out.println("New category created successfully!");
    }

    private void performTransaction(int clientId) {
        int senderAccountId = requestInt("Enter the ID of the account you are going to send money from:");
        int receiverAccountId = requestInt("Enter the ID of the account you are going to send money to:");
        BigDecimal amount = requestBigDecimal("Enter the amount:");
        List<Integer> categoryIds = requestCategoryIds();

        TransactionCommandCreation command = TransactionCommandCreation.builder()
                .senderAccountId(senderAccountId)
                .clientId(clientId)
                .categoryIds(categoryIds)
                .amount(amount)
                .receiverAccountId(receiverAccountId)
                .build();

        transactionService.performTransaction(command);
        System.out.println("Transaction completed successfully!");
    }

    private void updateTransactionCategory(int clientId) {
        int categoryId = requestInt("Enter the Category id:");
        String newName = requestString("Enter new Name for Category");

        transactionCategoryService.updateTransactionCategory(newName, categoryId, clientId);
        System.out.println("Category has been edited");
    }

    private void getInformationByIncome(int clientId) {
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

    private void getInformationByExpense(int clientId) {
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

    private String requestString(String title) {
        System.out.println(title);
        return scanner.next();
    }

    private int requestInt(String title) {
        System.out.println(title);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }

    private double requestDouble(String title) {
        System.out.println(title);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        double number = scanner.nextDouble();
        scanner.nextLine();
        return number;
    }

    private LocalDate requestLocalDate(String prompt) {
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

    private BigDecimal requestBigDecimal(String title) {
        System.out.println(title);
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        BigDecimal number = scanner.nextBigDecimal();
        scanner.nextLine();
        return number;
    }

    private List<Integer> requestCategoryIds() {
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
