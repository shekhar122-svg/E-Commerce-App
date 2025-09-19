// Save this as Accounts.java
package BankingManagement;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) {
        if (!account_exist(email)) {
            String sql = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.println("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.println("Enter initial amount:");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter security pin:");
            String security_pin = scanner.nextLine();

            try {
                long account_number = generateAccountNumber();
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, account_number);
                ps.setString(2, full_name);
                ps.setString(3, email);
                ps.setDouble(4, balance);
                ps.setString(5, security_pin);
                if (ps.executeUpdate() > 0) return account_number;
                else throw new RuntimeException("Account Creation Failed!");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Database error occurred while creating account.");
            }
        }
        throw new RuntimeException("Account Already Exists");
    }

    public long getAccount_number(String email) {
        String sql = "SELECT account_number FROM Accounts WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("account_number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT account_number FROM Accounts ORDER BY account_number DESC LIMIT 1");
            return rs.next() ? rs.getLong("account_number") + 1 : 10000100;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email) {
        String sql = "SELECT account_number FROM Accounts WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
