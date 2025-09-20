
package BankingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            while (true) {
                System.out.println("\n*** Welcome to Banking App ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");
                int choice1 = scanner.nextInt();
                scanner.nextLine();

                switch (choice1) {
                    case 1 -> user.register();
                    case 2 -> {
                        String email = user.login();
                        if (email != null) {
                            System.out.println("\nUser Logged In!");

                            if (!accounts.account_exist(email)) {
                                System.out.println("1. Open a new bank account");
                                System.out.println("2. Exit");
                                System.out.print("Enter choice: ");
                                int subChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (subChoice == 1) {
                                    long accountNumber = accounts.open_account(email);
                                    System.out.println("Account Created Successfully!");
                                    System.out.println("Your Account Number is: " + accountNumber);
                                } else break;
                            }

                            long accountNumber = accounts.getAccount_number(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println("\n--- Account Menu ---");
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter Your Choice: ");
                                choice2 = scanner.nextInt();
                                scanner.nextLine();

                                switch (choice2) {
                                    case 1 -> accountManager.debit_money(accountNumber);
                                    case 2 -> accountManager.credit_money(accountNumber);
                                    case 3 -> accountManager.transfer_money(accountNumber);
                                    case 4 -> accountManager.getBalance(accountNumber);
                                    case 5 -> System.out.println("Logging out...");
                                    default -> System.out.println("Enter a valid choice.");
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password.");
                        }
                    }
                    case 3 -> {
                        System.out.println("Thank you for using the banking system.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
