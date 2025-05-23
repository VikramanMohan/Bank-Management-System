package com.jspiders.featuresOfBMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    // Create method to register the user
    public void register() {
        sc.nextLine();
        System.out.println("Enter User Name : ");
        String fname = sc.nextLine();
        System.out.println("Enter User Email ID : ");
        String email = sc.nextLine();
        System.out.println("Enter User Password : ");
        String password = sc.nextLine();

        if (user_exist(email)) {
            System.out.println("!!!User Already Exists for this Email Address!!!");
            return;
        }

        String query = "INSERT INTO USER(FULL_NAME, EMAIL, PASSWORD) VALUES(?, ?, ?)";

        try (PreparedStatement psmt = con.prepareStatement(query)) {
            psmt.setString(1, fname);
            psmt.setString(2, email);
            psmt.setString(3, password);
            int row = psmt.executeUpdate();

            if (row > 0) {
                System.out.println("Registration Done Successfully!!!");
            } else {
                System.out.println("Registration Failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create method for user login
    public String login() {
        sc.nextLine();
        System.out.println("Enter User Email Id : ");
        String email = sc.nextLine();
        System.out.println("Enter User Password : ");
        String password = sc.nextLine();

        String query = "SELECT * FROM USER WHERE EMAIL = ? AND PASSWORD = ?";

        try (PreparedStatement psmt = con.prepareStatement(query)) {
            psmt.setString(1, email);
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                return email;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create method to check whether user exists or not
    public boolean user_exist(String email) {
        String query = "SELECT * FROM USER WHERE EMAIL = ?";

        try (PreparedStatement psmt = con.prepareStatement(query)) {
            psmt.setString(1, email);
            ResultSet rs = psmt.executeQuery();

            return rs.next(); // true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
