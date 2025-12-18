package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentDAO {

    public ObservableList<Student> getAllStudents() throws SQLException {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT s.*, u.username, u.password FROM students s " +
                    "JOIN users u ON s.user_id = u.id " +
                    "ORDER BY s.student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("department")
                );

                Integer routeId = rs.getObject("assigned_route_id", Integer.class);
                Integer busId = rs.getObject("assigned_bus_id", Integer.class);
                student.setAssignedRouteId(routeId);
                student.setAssignedBusId(busId);

                students.add(student);
            }
        }

        return students;
    }

    public void assignBusAndRoute(String studentId, int busId, int routeId) throws SQLException {
        String sql = "UPDATE students SET assigned_bus_id = ?, assigned_route_id = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            pstmt.setInt(2, routeId);
            pstmt.setString(3, studentId);
            pstmt.executeUpdate();

            System.out.println("✓ Bus and route assigned to student successfully!");
        }
    }

    public void removeAssignment(String studentId) throws SQLException {
        String sql = "UPDATE students SET assigned_bus_id = NULL, assigned_route_id = NULL WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            System.out.println("✓ Assignment removed from student!");
        }
    }

    public Student getStudentByStudentId(String studentId) throws SQLException {
        String sql = "SELECT s.*, u.username, u.password FROM students s " +
                    "JOIN users u ON s.user_id = u.id " +
                    "WHERE s.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("department")
                );

                Integer routeId = rs.getObject("assigned_route_id", Integer.class);
                Integer busId = rs.getObject("assigned_bus_id", Integer.class);
                student.setAssignedRouteId(routeId);
                student.setAssignedBusId(busId);

                return student;
            }
        }

        return null;
    }

    public ObservableList<Student> getUnassignedStudents() throws SQLException {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT s.*, u.username, u.password FROM students s " +
                    "JOIN users u ON s.user_id = u.id " +
                    "WHERE s.assigned_bus_id IS NULL OR s.assigned_route_id IS NULL " +
                    "ORDER BY s.student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("department")
                );
                students.add(student);
            }
        }
        return students;
    }
}

