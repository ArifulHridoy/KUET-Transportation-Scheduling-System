package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Request;
import com.example.kuet_transportation_and_schedueling_system.model.RequestStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class RequestDAO {

    public ObservableList<Request> getAllRequests() throws SQLException {
        ObservableList<Request> requests = FXCollections.observableArrayList();
        String sql = "SELECT r.*, s.name as student_name " +
                    "FROM requests r " +
                    "JOIN students s ON r.student_id = s.student_id " +
                    "ORDER BY r.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Request request = new Request(
                    rs.getInt("request_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    RequestStatus.valueOf(rs.getString("status"))
                );
                requests.add(request);
            }
        }
        return requests;
    }

    public ObservableList<Request> getPendingRequests() throws SQLException {
        ObservableList<Request> requests = FXCollections.observableArrayList();
        String sql = "SELECT r.*, s.name as student_name " +
                    "FROM requests r " +
                    "JOIN students s ON r.student_id = s.student_id " +
                    "WHERE r.status = 'PENDING' " +
                    "ORDER BY r.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Request request = new Request(
                    rs.getInt("request_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    RequestStatus.valueOf(rs.getString("status"))
                );
                requests.add(request);
            }
        }
        return requests;
    }

    public ObservableList<Request> getRequestsByStudentId(String studentId) throws SQLException {
        ObservableList<Request> requests = FXCollections.observableArrayList();
        String sql = "SELECT r.*, s.name as student_name " +
                    "FROM requests r " +
                    "JOIN students s ON r.student_id = s.student_id " +
                    "WHERE r.student_id = ? " +
                    "ORDER BY r.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Request request = new Request(
                    rs.getInt("request_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    RequestStatus.valueOf(rs.getString("status"))
                );
                requests.add(request);
            }
        }
        return requests;
    }

    public void addRequest(String studentId, String type, String description) throws SQLException {
        String sql = "INSERT INTO requests (student_id, type, description, status) VALUES (?, ?, ?, 'PENDING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, type);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            System.out.println("✓ Request submitted successfully!");
        }
    }

    public void updateRequestStatus(int requestId, RequestStatus status) throws SQLException {
        String sql = "UPDATE requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
            System.out.println("✓ Request status updated to: " + status);
        }
    }

    public void approveRequest(int requestId) throws SQLException {
        updateRequestStatus(requestId, RequestStatus.APPROVED);
    }

    public void rejectRequest(int requestId) throws SQLException {
        updateRequestStatus(requestId, RequestStatus.REJECTED);
    }

    public void deleteRequest(int requestId) throws SQLException {
        String sql = "DELETE FROM requests WHERE request_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            pstmt.executeUpdate();
            System.out.println("✓ Request deleted!");
        }
    }

    public int getPendingRequestCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM requests WHERE status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}

