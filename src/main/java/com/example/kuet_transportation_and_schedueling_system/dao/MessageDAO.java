package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;

public class MessageDAO {

    public ObservableList<Message> getMessagesByUserId(int userId) throws SQLException {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        String sql = "SELECT m.*, u.username as sender_name " +
                    "FROM messages m " +
                    "LEFT JOIN users u ON u.id = 1 " +  // Messages from admin
                    "WHERE m.user_id = ? " +
                    "ORDER BY m.message_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getTimestamp("message_date").toLocalDateTime()
                );
                message.setSenderName("Admin");
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        }

        return messages;
    }

    public void sendMessage(int userId, String message) throws SQLException {
        String sql = "INSERT INTO messages (user_id, message, message_date) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
            System.out.println("✓ Message sent successfully!");
        }
    }

    public void sendMessageToAllStudents(String message) throws SQLException {
        String sql = "INSERT INTO messages (user_id, message, message_date) " +
                    "SELECT u.id, ?, ? FROM users u WHERE u.role = 'student'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, message);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            int count = pstmt.executeUpdate();

            System.out.println("✓ Message sent to " + count + " students!");
        }
    }

    public void markAsRead(int messageId) throws SQLException {
        String sql = "UPDATE messages SET is_read = TRUE WHERE message_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
        }
    }

    public void deleteMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM messages WHERE message_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messageId);
            pstmt.executeUpdate();
            System.out.println("✓ Message deleted!");
        }
    }

    public int getUnreadMessageCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM messages WHERE user_id = ? AND is_read = FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public ObservableList<Message> getLatestMessages(int userId, int limit) throws SQLException {
        ObservableList<Message> messages = FXCollections.observableArrayList();
        String sql = "SELECT m.*, u.username as sender_name " +
                    "FROM messages m " +
                    "LEFT JOIN users u ON u.id = 1 " +
                    "WHERE m.user_id = ? " +
                    "ORDER BY m.message_date DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getTimestamp("message_date").toLocalDateTime()
                );
                message.setSenderName("Admin");
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        }
        return messages;
    }
}

