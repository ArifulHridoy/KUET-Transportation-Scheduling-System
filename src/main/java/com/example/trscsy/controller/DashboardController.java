package com.example.trscsy.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label totalBusesLabel;
    @FXML private Label activeBusesLabel;
    @FXML private Label totalDriversLabel;
    @FXML private Label activeDriversLabel;
    @FXML private Label totalRoutesLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label tripsTodayLabel;
    @FXML private Label tripsWeekLabel;

    @FXML public void initialize() {}

    // Added missing handler referenced by dashboard.fxml (onAction="#handleDashboard")
    @FXML public void handleDashboard() {
        // No-op: already on dashboard. Could be expanded to refresh stats.
    }

    @FXML private void handleManageBuses() {
        loadView("bus-management.fxml", "Bus Management");
    }

    @FXML private void handleManageDrivers() {
        loadView("driver-management.fxml", "Driver Management");
    }

    @FXML private void handleManageRoutes() {
        loadView("route-management.fxml", "Route Management");
    }

    @FXML private void handleManageSchedules() {
        loadView("schedule-management.fxml", "Schedule Management");
    }

    @FXML private void handleStudentAssignment() {
        loadView("student-assignment.fxml", "Student Assignment");
    }

    @FXML private void handleTravelHistory() {
        loadView("travel-history.fxml", "Travel History");
    }

    @FXML private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trscsy/fxml/loginAdmin.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) totalBusesLabel.getScene().getWindow();
                    Scene scene = new Scene(root, 700, 550);
                    scene.getStylesheets().add(getClass().getResource("/com/example/trscsy/style/style.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setTitle("KUET Transportation System - Login");
                    stage.setMaximized(false);
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to logout: " + e.getMessage());
                }
            }
        });
    }

    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trscsy/fxml/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) totalBusesLabel.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(getClass().getResource("/com/example/trscsy/style/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("KUET Transportation System - " + title);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load " + title + ": " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
