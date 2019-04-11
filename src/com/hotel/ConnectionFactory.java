package com.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/hotel?useSSL=false";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "6259";

    public static Connection getConnection() {
        try {
            // load the Driver Class
            Class.forName("com.mysql.cj.jdbc.Driver");
            // create the connection now
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please include JDBC MySQL jar in classpath", e);
        }
    }
}