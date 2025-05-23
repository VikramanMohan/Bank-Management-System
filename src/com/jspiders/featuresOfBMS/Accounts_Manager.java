package com.jspiders.featuresOfBMS;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Accounts_Manager {
	private Connection con ;
	private Scanner sc ;
	public Accounts_Manager(Connection con, Scanner sc) {
		this.con=con;
		this.sc=sc;
	}
	// create method for deposit amount
	public void credit_money(long account_number) throws SQLException {
		sc.nextLine();
		System.out.println("Enter Amount : ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter Pin : ");
		String pin = sc.nextLine();
		try {
			con.setAutoCommit(false);
			if(account_number!=0) {
				String query = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ? AND PIN = ?";
				PreparedStatement psmt1 = con.prepareStatement(query);
				psmt1.setLong(1, account_number);
				psmt1.setString(2, pin);
				ResultSet rs = psmt1.executeQuery();
				if(rs.next()) {
					String credit ="UPDATE ACCOUNTS SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";
					psmt1 = con.prepareStatement(credit);
					psmt1.setDouble(1, amount);
					psmt1.setLong(2, account_number);
					int row = psmt1.executeUpdate();
					if(row > 0) {
						System.out.println("Rs ."+amount+"credited successfully");
						con.commit();
						con.setAutoCommit(true);
					}
					else {
						System.out.println("Transaction failed!!!");
						con.rollback();
						con.setAutoCommit(true);
					}
				}
				else {
					System.out.println("Invalid PIN");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true); // Thrown SQLException
	}
}
