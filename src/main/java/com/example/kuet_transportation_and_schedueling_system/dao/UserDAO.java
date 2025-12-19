package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Admin;
import com.example.kuet_transportation_and_schedueling_system.model.Student;
import com.example.kuet_transportation_and_schedueling_system.model.User;
import com.example.kuet_transportation_and_schedueling_system.util.InvalidLoginException;
import com.example.kuet_transportation_and_schedueling_system.util.UserAlreadyExistsException;

import java.sql.*;

public class UserDAO {

    public void registerStudent(String username, String password, String studentId,
                               String name, String department) throws UserAlreadyExistsException, SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check if username already exists - using same connection
            if (usernameExistsWithConnection(conn, username)) {
                throw new UserAlreadyExistsException("Username '" + username + "' already exists!");
            }

            // Check if student ID already exists - using same connection
            if (studentIdExistsWithConnection(conn, studentId)) {
                throw new UserAlreadyExistsException("Student ID '" + studentId + "' already registered!");
            }

            // Insert into users table
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
            int userId;
            try (PreparedStatement pstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password); // In production, hash the password!
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to get user ID");
                }
            }

            // Insert into students table
            String studentSql = "INSERT INTO students (student_id, name, department, user_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(studentSql)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, name);
                pstmt.setString(3, department);
                pstmt.setInt(4, userId);
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            System.out.println("✓ Student registered successfully!");

        } catch (SQLException | UserAlreadyExistsException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("[ERROR] Database connection failed during registration");
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public User login(String username, String password) throws InvalidLoginException, SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        System.out.println("=== UserDAO.login() ===");
        System.out.println("Attempting login with username: [" + username + "]");
        System.out.println("Password length: " + password.length());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            System.out.println("Executing query: " + sql);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                System.out.println("User found! ID: " + userId + ", Role: " + role);

                if ("admin".equals(role)) {
                    return new Admin(userId, username, password);
                } else if ("student".equals(role)) {
                    return getStudentDetails(userId, username, password);
                }
            }

            System.out.println("No matching user found in database");
            throw new InvalidLoginException("Invalid username or password!");
        } catch (SQLException e) {
            System.out.println("SQL Exception during login: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private Student getStudentDetails(int userId, String username, String password) throws SQLException {
        String sql = "SELECT * FROM students WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String studentId = rs.getString("student_id");
                String name = rs.getString("name");
                String department = rs.getString("department");

                Student student = new Student(userId, username, password, studentId, name, department);

                // Get assigned route and bus if any
                Integer routeId = rs.getObject("assigned_route_id", Integer.class);
                Integer busId = rs.getObject("assigned_bus_id", Integer.class);
                student.setAssignedRouteId(routeId);
                student.setAssignedBusId(busId);

                return student;
            }

            return null;
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private boolean usernameExistsWithConnection(Connection conn, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public boolean studentIdExists(String studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private boolean studentIdExistsWithConnection(Connection conn, String studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public void createDefaultAdmin() throws SQLException {
        if (!usernameExists("admin")) {
            String sql = "INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.executeUpdate();
                System.out.println("✓ Default admin created (username: admin, password: admin123)");
            }
        }
    }
}

