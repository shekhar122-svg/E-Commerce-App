// Save this as User.java
package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine(); // consume newline
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userExists(email)) {
            System.out.println("User already exists for this email address!");
            return;
        }

        String registerQuery = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(registerQuery)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, password);
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Registration successful!" : "Failed to register user.");
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    public String login() {
        scanner.nextLine(); // consume newline
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String loginQuery = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(loginQuery)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? email : null;
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }

    private boolean userExists(String email) {
        String checkQuery = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(checkQuery)) {
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }
}
