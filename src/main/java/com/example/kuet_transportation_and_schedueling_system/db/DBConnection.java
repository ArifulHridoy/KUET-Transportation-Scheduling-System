package com.example.kuet_transportation_and_schedueling_system.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/kuet_transport?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Private constructor to prevent instantiation
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Database connected successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Please add MySQL Connector to your project.", e);
        } catch (SQLException e) {
            System.err.println("✗ Cannot connect to database!");
            System.err.println("  Reason: " + e.getMessage());
            System.err.println("\nPossible solutions:");
            System.err.println("  1. Make sure MySQL Server is running");
            System.err.println("  2. Check if database 'kuet_transport' exists");
            System.err.println("  3. Verify MySQL username/password");
            System.err.println("  4. Check if MySQL is running on port 3306");
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✓ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}

