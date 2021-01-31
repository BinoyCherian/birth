package com.birth.back.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.birth.constants.StringConstants;

public class BackendDatabase {
	
	public static final int MAX_CONNECTION = 99;
	public static final String URL ;
	public static final String DB_URL_STRING = "DATABASE_URL";
	private static Connection connection;
	
	private BackendDatabase() {
	}
	
	static {
		URL = (fetchConnectionProperties()).getProperty("DATABASE_CONNECTSTRING");
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) { }
		
		connection = fetchConnection();
	}
	
	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		BackendDatabase.connection = connection;
	}

	public static Properties fetchConnectionProperties() {
		
		Properties prop = null;
		
		 try (InputStream input = BackendDatabase.class.getResourceAsStream("/database/config.properties")) {

	            prop  = new Properties();
	            if (input == null) {
	                System.out.println("Sorry, unable to find : config.properties");
	            }

	            // load a properties file
	            prop.load(input);
	            prop.put("DATABASE_CONNECTSTRING", prepareConnectionString(prop));

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		 return prop;
	}
	
	private static String prepareConnectionString(Properties prop) {
		
		String url = StringConstants.EMPTY_STRING;
		try {
			url = ((String) prop.get(DB_URL_STRING)).endsWith("/")? (String) prop.get(DB_URL_STRING): (String) prop.get(DB_URL_STRING) + "/";
			url = url + prop.getProperty("DATABASE_NAME") + "?" + "user="+ prop.getProperty("DATABASE_USER")+ "&password="+ prop.getProperty("DATABASE_PASSWORD");
		}catch(Exception e) {}
		
		return url;
	}

	private static Connection fetchConnection() {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
