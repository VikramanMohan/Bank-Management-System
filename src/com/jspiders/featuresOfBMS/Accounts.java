package com.jspiders.featuresOfBMS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class Accounts {
	private Connection con ;
	private Scanner sc;
	public Accounts(Connection con , Scanner sc) {  // Constructor
		this.con=con;
		this.sc=sc;

	}
	// create a method to open account 
	public long open_account(String email) {
		if(!account_exist(email)) {
			String query = "INSERT INTO ACCOUNTS(ACCOUNT_NUMBER,FULL_NAME,EMAIL,BALANCE,PIN) VALUES(?,?,?,?,?)";
			sc.nextLine(); // next line
			System.out.println("Enter Full Name : ");
			String fname = sc.nextLine();
			sc.nextLine();
			System.out.println("Enter Initial Amount : ");
			double balance = sc.nextDouble();
			System.out.println("Enter Security Pin : ");
			String pin = sc.nextLine();
			try {
				long account_number = generateAccountNumber(); // it is an method which is used to generate ACN
				PreparedStatement psmt = con.prepareStatement(query);
				psmt.setLong(1, account_number);
				psmt.setString(2, fname);
				psmt.setString(3, email);
				psmt.setDouble(4, balance);
				psmt.setString(4, pin);
				int row = psmt.executeUpdate(); //execute the query
				if(row > 0) {
					return account_number;
				}
				else {
					throw new RuntimeException("Account creation failed");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}

		}
		throw new RuntimeException("!!!Account Already Exist!!!");
	}
	// CREATE A METHOD FOR GETTING ACCOUNT NUMBER
	public long getAccount_number(String email) {
		String query =  "SELECT ACCOUNT_NUMBER FROM ACCOUNTS WHERE EMAIL = ?";
		try {
			PreparedStatement psmt = con.prepareStatement(query);
			psmt.setString(1, email);
			ResultSet rs = psmt.executeQuery();
			if(rs.next()) {
				return rs.getLong("account_number");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account doesn't Exist's");
	}
	// create method for generating account number
	private long generateAccountNumber() {
		try {
			String query = "SELECT ACCOUNT_NUMBER FROM ACCOUNTS ORDER BY ACCOUNT_NUMBER DESC LIMIT 1";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				long last_account_number = rs.getLong("account_number");
				return last_account_number+1;
			}
			else {
				return 1000001001; // return's long data
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 1000001001;
	} // creating a method to check account exist or not
	public boolean account_exist(String email) {
		String query = "SELECT ACCOUNT_NUMBER FROM ACCOUNTS WHERE EMAIL = ?";	
		try {
			PreparedStatement psmt = con.prepareStatement(query);
			psmt.setString(1, email);
			ResultSet rs = psmt.executeQuery();
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
