package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_NAME = "flexmanager";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:5123/" + DATABASE_NAME;

    private Connection databaseLink;

    public Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            databaseLink = DriverManager.getConnection(URL, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Database connection successful!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw new SQLException("Database connection error", e);
        }
        return databaseLink;
    }
}
