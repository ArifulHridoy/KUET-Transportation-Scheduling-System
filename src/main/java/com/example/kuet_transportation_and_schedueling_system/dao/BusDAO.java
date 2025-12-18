package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Bus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BusDAO {

    public ObservableList<Bus> getAllBuses() throws SQLException {
        ObservableList<Bus> buses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM buses ORDER BY bus_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Bus bus = new Bus(
                    rs.getInt("bus_id"),
                    rs.getString("bus_number"),
                    rs.getInt("capacity")
                );
                buses.add(bus);
            }
        }

        return buses;
    }

    public void addBus(String busNumber, int capacity) throws SQLException {
        String sql = "INSERT INTO buses (bus_number, capacity) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, busNumber);
            pstmt.setInt(2, capacity);
            pstmt.executeUpdate();

            System.out.println("✓ Bus added successfully!");
        }
    }

    public void updateBus(int busId, String busNumber, int capacity) throws SQLException {
        String sql = "UPDATE buses SET bus_number = ?, capacity = ? WHERE bus_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, busNumber);
            pstmt.setInt(2, capacity);
            pstmt.setInt(3, busId);
            pstmt.executeUpdate();
            System.out.println("✓ Bus updated successfully!");
        }
    }

    public void deleteBus(int busId) throws SQLException {
        String sql = "DELETE FROM buses WHERE bus_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Bus deleted successfully!");
            } else {
                System.out.println("Bus not found!");
            }
        }
    }

    public Bus getBusById(int busId) throws SQLException {
        String sql = "SELECT * FROM buses WHERE bus_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Bus(
                    rs.getInt("bus_id"),
                    rs.getString("bus_number"),
                    rs.getInt("capacity")
                );
            }
        }

        return null;
    }

    public boolean busNumberExists(String busNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM buses WHERE bus_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, busNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}

