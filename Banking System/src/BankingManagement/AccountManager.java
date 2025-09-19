// Save this as AccountManager.java
package BankingManagement;

import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin:");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreparedStatement update = connection.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ?");
                update.setDouble(1, amount);
                update.setLong(2, account_number);
                if (update.executeUpdate() > 0) {
                    System.out.println("Rs. " + amount + " credited successfully.");
                    connection.commit();
                } else {
                    System.out.println("Transaction Failed.");
                    connection.rollback();
                }
            } else {
                System.out.println("Invalid security pin.");
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin:");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement update = connection.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ?");
                update.setDouble(1, amount);
                update.setLong(2, account_number);
                if (update.executeUpdate() > 0) {
                    System.out.println("Rs. " + amount + " debited successfully.");
                    connection.commit();
                } else {
                    System.out.println("Transaction failed.");
                    connection.rollback();
                }
            } else {
                System.out.println("Invalid pin or insufficient balance.");
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter receiver account number:");
        long receiver_account = scanner.nextLong();
        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin:");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);

            PreparedStatement senderStmt = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            senderStmt.setLong(1, sender_account_number);
            senderStmt.setString(2, pin);
            ResultSet senderResult = senderStmt.executeQuery();

            if (senderResult.next() && senderResult.getDouble("balance") >= amount) {
                PreparedStatement debitStmt = connection.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ?");
                PreparedStatement creditStmt = connection.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ?");

                debitStmt.setDouble(1, amount);
                debitStmt.setLong(2, sender_account_number);
                creditStmt.setDouble(1, amount);
                creditStmt.setLong(2, receiver_account);

                int debitRows = debitStmt.executeUpdate();
                int creditRows = creditStmt.executeUpdate();

                if (debitRows > 0 && creditRows > 0) {
                    connection.commit();
                    System.out.println("Rs. " + amount + " transferred successfully.");
                } else {
                    System.out.println("Transaction failed.");
                    connection.rollback();
                }
            } else {
                System.out.println("Invalid pin or insufficient balance.");
                connection.rollback();
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.println("Enter security pin:");
        String pin = scanner.nextLine();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Your current balance is: Rs. " + balance);
            } else {
                System.out.println("Invalid security pin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
