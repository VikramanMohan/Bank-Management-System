package com.jspiders.featuresOfBMS;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
        	String dburl="jdbc:mysql://localhost:3306/add_a3?user=root&password=Root@2002";
            Connection con=DriverManager.getConnection(dburl);
            BankApp app = new BankApp(con, sc);
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
