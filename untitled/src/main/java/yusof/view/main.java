package yusof.view;

import yusof.service.AccountDTO;
import yusof.service.AccountService;
import yusof.service.AuthorizationService;
import yusof.service.TransactionTypeService;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        AuthorizationService authorizationService = new AuthorizationService();

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
        int initialBalance = requestInt("Enter the initial balance:");

        accountService.createAccount(accountName, initialBalance, clientId);
        System.out.println("New account created successfully!");
    }

    private static void deleteAccount(AccountService accountService, int clientId) {
        int accountId = requestInt("Enter the account id:");
        accountService.deleteAccount(accountId, clientId);
        System.out.println("account has been delete successfully");
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
            System.out.println("5. Logout");

            int dashboardChoice = requestInt("Enter your choice:");
            AccountService accountService =
                    new AccountService();
            TransactionTypeService transactionTypeService = new TransactionTypeService();

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
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Incorrect choice!");
            }
        }
    }

    static String requestString(String title) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        return scanner.next();
    }

    static int requestInt(String title) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(title);
        return scanner.nextInt();
    }
}
