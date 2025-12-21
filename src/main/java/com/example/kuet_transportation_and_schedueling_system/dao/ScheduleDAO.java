package com.example.kuet_transportation_and_schedueling_system.dao;
import com.example.kuet_transportation_and_schedueling_system.db.DBConnection;
import com.example.kuet_transportation_and_schedueling_system.model.Schedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDAO {

    public ObservableList<Schedule> getAllSchedules() throws SQLException {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        String sql = "SELECT s.*, b.bus_number, r.name as route_name " +
                    "FROM schedules s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "ORDER BY s.schedule_date DESC, s.schedule_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("route_id"),
                    rs.getDate("schedule_date").toLocalDate(),
                    rs.getTime("schedule_time").toLocalTime()
                );
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setRouteName(rs.getString("route_name"));
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    public void addSchedule(int busId, int routeId, LocalDate date, LocalTime time) throws SQLException {
        String sql = "INSERT INTO schedules (bus_id, route_id, schedule_date, schedule_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            pstmt.setInt(2, routeId);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.setTime(4, Time.valueOf(time));
            pstmt.executeUpdate();
            System.out.println("✓ Schedule added successfully!");
        }
    }

    public void updateSchedule(int scheduleId, int busId, int routeId, LocalDate date, LocalTime time) throws SQLException {
        String sql = "UPDATE schedules SET bus_id = ?, route_id = ?, schedule_date = ?, schedule_time = ? WHERE schedule_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, busId);
            pstmt.setInt(2, routeId);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.setTime(4, Time.valueOf(time));
            pstmt.setInt(5, scheduleId);
            pstmt.executeUpdate();

            System.out.println("✓ Schedule updated successfully!");
        }
    }

    public void deleteSchedule(int scheduleId) throws SQLException {
        String sql = "DELETE FROM schedules WHERE schedule_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Schedule deleted successfully!");
            } else {
                System.out.println("Schedule not found!");
            }
        }
    }

    public ObservableList<Schedule> getStudentSchedules(int studentUserId) throws SQLException {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();

        // First, get student's assignments for debugging
        String debugSql = "SELECT st.student_id, st.assigned_route_id, st.assigned_bus_id FROM students st WHERE st.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement debugStmt = conn.prepareStatement(debugSql)) {
            debugStmt.setInt(1, studentUserId);
            ResultSet debugRs = debugStmt.executeQuery();
            if (debugRs.next()) {
                System.out.println("DEBUG - Student ID: " + debugRs.getString("student_id"));
                System.out.println("DEBUG - Assigned Route ID: " + debugRs.getObject("assigned_route_id"));
                System.out.println("DEBUG - Assigned Bus ID: " + debugRs.getObject("assigned_bus_id"));
            }
        } catch (Exception e) {
            System.err.println("Debug query failed: " + e.getMessage());
        }

        // Show schedules for the student's assigned route and/or bus
        String sql = "SELECT s.*, b.bus_number, r.name as route_name " +
                    "FROM schedules s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "JOIN students st ON st.user_id = ? " +
                    "WHERE (st.assigned_route_id IS NOT NULL AND s.route_id = st.assigned_route_id) " +
                    "   OR (st.assigned_bus_id IS NOT NULL AND s.bus_id = st.assigned_bus_id) " +
                    "ORDER BY s.schedule_date, s.schedule_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentUserId);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"),
                    rs.getInt("bus_id"),
                    rs.getInt("route_id"),
                    rs.getDate("schedule_date").toLocalDate(),
                    rs.getTime("schedule_time").toLocalTime()
                );
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setRouteName(rs.getString("route_name"));
                schedules.add(schedule);
                count++;
                System.out.println("DEBUG - Found schedule: Route=" + rs.getString("route_name") +
                                 ", Bus=" + rs.getString("bus_number") +
                                 ", Date=" + rs.getDate("schedule_date"));
            }
            System.out.println("DEBUG - Total schedules found: " + count);
        }
        return schedules;
    }
}

