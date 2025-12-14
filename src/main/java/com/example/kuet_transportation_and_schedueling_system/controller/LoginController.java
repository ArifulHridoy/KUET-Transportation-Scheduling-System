package com.example.kuet_transportation_and_schedueling_system.controller;

import com.example.kuet_transportation_and_schedueling_system.dao.UserDAO;
import com.example.kuet_transportation_and_schedueling_system.model.User;
import com.example.kuet_transportation_and_schedueling_system.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;

    @FXML private PasswordField passwordField;

    @FXML private Label errorLabel;

    @FXML private Button loginButton;

    @FXML private Hyperlink registerLink;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password!");
            return;
        }

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password length: " + password.length());
        System.out.println("====================");

        try {
            User user = userDAO.login(username, password);

            SessionManager.getInstance().setCurrentUser(user);

            Logger.getInstance().info("Login successful: " + username + " (" + user.getRole() + ")");

            try {
                if ("admin".equals(user.getRole())) {
                    SceneManager.switchScene("/com/example/kuet_transportation_and_schedueling_system/view/AdminDashboard.fxml",
                                            "Admin Dashboard");
                } else if ("student".equals(user.getRole())) {
                    SceneManager.switchScene("/com/example/kuet_transportation_and_schedueling_system/view/StudentDashboard.fxml",
                                            "Student Dashboard");
                }
            } catch (Exception dashboardError) {
                Logger.getInstance().error("Dashboard loading error: " + dashboardError.getMessage());
                showError("Dashboard failed to load. Please contact support.\n\nError: " + dashboardError.getMessage());
                dashboardError.printStackTrace();
            }

        } catch (InvalidLoginException e) {
            showError(e.getMessage());
            Logger.getInstance().warning("Failed login attempt: " + username);
            System.out.println("Login failed: " + e.getMessage());
        } catch (java.sql.SQLException e) {
            showError("Database Connection Error!\n\n" +
                     "MySQL Server is not running or database doesn't exist.\n\n" +
                     "Please:\n" +
                     "1. Start MySQL Server\n" +
                     "2. Run database_schema.sql to create database\n" +
                     "3. Try again");
            Logger.getInstance().error("Database connection failed during login");
            e.printStackTrace();
        }
    }

    @FXML private void handleRegister(ActionEvent event) {
        try {
            SceneManager.switchScene("/com/example/kuet_transportation_and_schedueling_system/view/Register.fxml",
                                    "Student Registration");
        } catch (IOException e) {
            showError("Failed to open registration page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }
}

