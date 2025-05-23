package com.jspiders.featuresOfBMS;

import java.sql.*;
import java.util.Scanner;

public class BankApp {
    private Connection con;
    private Scanner sc;
    private User user;

    public BankApp(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
        this.user = new User(con, sc);
    }

    public void start() {
        while (true) {
            System.out.println("\n----- Welcome to Bank Management System -----");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    user.register();
                    break;
                case 2:
                    String email = user.login();
                    if (email != null) {
                        System.out.println("Login successful!");
                        userMenu(email);
                    } else {
                        System.out.println("Invalid credentials!");
                    }
                    break;
                case 3:
                    System.out.println("Thank you for using the Bank App.");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void userMenu(String email) {
        while (true) {
            System.out.println("\n----- User Dashboard -----");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    deposit(email);
                    break;
                case 2:
                    withdraw(email);
                    break;
                case 3:
                    checkBalance(email);
                    break;
                case 4:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void deposit(String email) {
        System.out.print("Enter deposit amount: ");
        double amount = sc.nextDouble();
        String query = "UPDATE BANK_ACCOUNT SET BALANCE = BALANCE + ? WHERE EMAIL = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setString(2, email);
            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("Amount deposited successfully!");
            } else {
                // New user, create record
                String insert = "INSERT INTO BANK_ACCOUNT (EMAIL, BALANCE) VALUES (?, ?)";
                try (PreparedStatement insertPs = con.prepareStatement(insert)) {
                    insertPs.setString(1, email);
                    insertPs.setDouble(2, amount);
                    insertPs.executeUpdate();
                    System.out.println("Account created and amount deposited!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void withdraw(String email) {
        System.out.print("Enter withdrawal amount: ");
        double amount = sc.nextDouble();
        double balance = getBalance(email);

        if (balance < amount) {
            System.out.println("Insufficient balance!");
            return;
        }

        String query = "UPDATE BANK_ACCOUNT SET BALANCE = BALANCE - ? WHERE EMAIL = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setString(2, email);
            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("Amount withdrawn successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkBalance(String email) {
        double balance = getBalance(email);
        System.out.println("Current balance: ₹" + balance);
    }

    private double getBalance(String email) {
        String query = "SELECT BALANCE FROM BANK_ACCOUNT WHERE EMAIL = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("BALANCE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
