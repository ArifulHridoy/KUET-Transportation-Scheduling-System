package com.example.kuet_transportation_and_schedueling_system.controller;

import com.example.kuet_transportation_and_schedueling_system.dao.UserDAO;
import com.example.kuet_transportation_and_schedueling_system.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;

    @FXML private PasswordField passwordField;

    @FXML private PasswordField confirmPasswordField;

    @FXML private TextField studentIdField;

    @FXML private TextField nameField;

    @FXML private TextField departmentField;

    @FXML private ComboBox<String> departmentCombo;

    @FXML private Label errorLabel;

    @FXML private Label successLabel;

    @FXML private Button registerButton;

    @FXML private Hyperlink loginLink;

    private UserDAO userDAO;

    public RegisterController() {
        this.userDAO = new UserDAO();
    }

    @FXML public void initialize() {
        // Only initialize labels if they exist in FXML
        if (errorLabel != null) {
            errorLabel.setVisible(false);
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        if (successLabel != null) {
            successLabel.setVisible(false);
            successLabel.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String department = departmentCombo != null ?
            (departmentCombo.getValue() != null ? departmentCombo.getValue() : "") :
            (departmentField != null ? departmentField.getText().trim() : "");

        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
        if (successLabel != null) {
            successLabel.setVisible(false);
        }

        if (!validateInputs(username, password, confirmPassword, studentId, name, department)) {
            return;
        }

        try {
            userDAO.registerStudent(username, password, studentId, name, department);

            Logger.getInstance().info("New student registered: " + studentId + " - " + name);

            showSuccess("Registration successful! You can now login.");

            clearForm();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        try {
                            handleBackToLogin(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (UserAlreadyExistsException e) {
            showError(e.getMessage());
        } catch (java.sql.SQLException e) {
            showError("Database Connection Error!\n\n" +
                     "MySQL Server is not running or database doesn't exist.\n\n" +
                     "Please:\n" +
                     "1. Start MySQL Server\n" +
                     "2. Run database_schema.sql to create database\n" +
                     "3. Try again");
            Logger.getInstance().error("Database connection failed during registration");
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
            Logger.getInstance().error("Registration error: " + e.getMessage());
        }
    }

    private boolean validateInputs(String username, String password, String confirmPassword,
                                   String studentId, String name, String department) {

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            studentId.isEmpty() || name.isEmpty() || department.isEmpty()) {
            showError("All fields are required!");
            return false;
        }

        if (!Validator.isValidUsername(username)) {
            showError(Validator.getUsernameValidationMessage());
            return false;
        }

        if (!Validator.isValidPassword(password)) {
            showError(Validator.getPasswordValidationMessage());
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            return false;
        }

        if (!Validator.isValidStudentId(studentId)) {
            showError(Validator.getStudentIdValidationMessage());
            return false;
        }

        if (!Validator.isNotEmpty(name)) {
            showError("Please enter your name!");
            return false;
        }

        if (!Validator.isNotEmpty(department)) {
            showError("Please enter your department!");
            return false;
        }

        return true;
    }

    @FXML private void handleBackToLogin(ActionEvent event) {
        try {
            SceneManager.switchScene("/com/example/kuet_transportation_and_schedueling_system/view/Login.fxml",
                                    "Login");
        } catch (IOException e) {
            showError("Failed to open login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
        if (successLabel != null) {
            successLabel.setVisible(false);
        }
    }

    private void showSuccess(String message) {
        if (successLabel != null) {
            successLabel.setText(message);
            successLabel.setVisible(true);
        }
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        studentIdField.clear();
        nameField.clear();
        departmentField.clear();
    }
}

