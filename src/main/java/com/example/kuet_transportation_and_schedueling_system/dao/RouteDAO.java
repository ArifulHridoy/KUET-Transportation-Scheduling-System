package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class RouteDAO {

    public ObservableList<Route> getAllRoutes() throws SQLException {
        ObservableList<Route> routes = FXCollections.observableArrayList();
        String sql = "SELECT * FROM routes ORDER BY route_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Route route = new Route(
                    rs.getInt("route_id"),
                    rs.getString("name"),
                    rs.getString("start_point"),
                    rs.getString("end_point")
                );
                routes.add(route);
            }
        }

        return routes;
    }

    public void addRoute(String name, String startPoint, String endPoint) throws SQLException {
        String sql = "INSERT INTO routes (name, start_point, end_point) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, startPoint);
            pstmt.setString(3, endPoint);
            pstmt.executeUpdate();

            System.out.println("✓ Route added successfully!");
        }
    }

    public void updateRoute(int routeId, String name, String startPoint, String endPoint) throws SQLException {
        String sql = "UPDATE routes SET name = ?, start_point = ?, end_point = ? WHERE route_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, startPoint);
            pstmt.setString(3, endPoint);
            pstmt.setInt(4, routeId);
            pstmt.executeUpdate();

            System.out.println("✓ Route updated successfully!");
        }
    }

    public void deleteRoute(int routeId) throws SQLException {
        String sql = "DELETE FROM routes WHERE route_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, routeId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Route deleted successfully!");
            } else {
                System.out.println("Route not found!");
            }
        }
    }

    public Route getRouteById(int routeId) throws SQLException {
        String sql = "SELECT * FROM routes WHERE route_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, routeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Route(
                    rs.getInt("route_id"),
                    rs.getString("name"),
                    rs.getString("start_point"),
                    rs.getString("end_point")
                );
            }
        }
        return null;
    }
}

