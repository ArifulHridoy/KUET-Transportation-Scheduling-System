package com.example.kuet_transportation_and_schedueling_system.controller;
import com.example.kuet_transportation_and_schedueling_system.dao.*;
import com.example.kuet_transportation_and_schedueling_system.model.*;
import com.example.kuet_transportation_and_schedueling_system.util.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class StudentDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label studentIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label departmentLabel;
    @FXML private Button logoutBtn;
    
    @FXML private Label assignedBusLabel;
    @FXML private Label assignedRouteLabel;
    @FXML private Label routeDetailsLabel;
    @FXML private Label routeStartLabel;
    @FXML private Label routeEndLabel;
    @FXML private Label busCapacityLabel;
    
    @FXML private TableView<Schedule> scheduleTable;
    @FXML private TableColumn<Schedule, String> scheduleBusCol;
    @FXML private TableColumn<Schedule, String> scheduleRouteCol;
    @FXML private TableColumn<Schedule, LocalDate> scheduleDateCol;
    @FXML private TableColumn<Schedule, LocalTime> scheduleTimeCol;
    @FXML private Button refreshSchedulesBtn;
    
    @FXML private TableView<Request> requestsTable;
    @FXML private TableColumn<Request, Integer> requestIdCol;
    @FXML private TableColumn<Request, String> requestTypeCol;
    @FXML private TableColumn<Request, String> requestDescCol;
    @FXML private TableColumn<Request, RequestStatus> requestStatusCol;
    @FXML private ComboBox<String> requestTypeCombo;
    @FXML private TextArea requestDescriptionArea;
    @FXML private Label requestStatusLabel;
    @FXML private Button submitRequestBtn;
    @FXML private Button refreshRequestsBtn;
    
    @FXML private TableView<Message> messagesTable;
    @FXML private TableColumn<Message, LocalDateTime> messageDateCol;
    @FXML private TableColumn<Message, String> messageContentCol;
    @FXML private Button refreshMessagesBtn;
    @FXML private Label unreadCountLabel;
    
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final MessageDAO messageDAO = new MessageDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final RouteDAO routeDAO = new RouteDAO();
    private final BusDAO busDAO = new BusDAO();
    
    private Student currentStudent;
    
    private Thread messageRefreshThread;
    private volatile boolean keepRefreshing = true;

    @FXML public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser instanceof Student) {
            currentStudent = (Student) currentUser;

            // Set welcome message
            welcomeLabel.setText("Welcome, " + currentStudent.getName());
            if (nameLabel != null) {
                nameLabel.setText(currentStudent.getName());
            }
            studentIdLabel.setText("ID: " + currentStudent.getStudentId());
            departmentLabel.setText("Dept: " + currentStudent.getDepartment());

            // Initialize tables
            initializeSchedulesTable();
            initializeRequestsTable();
            initializeMessagesTable();
            
            loadProfileInfo();
            loadSchedules();
            loadRequests();
            loadMessages();
            
            startMessageRefreshThread();
        }
    }

    //Profile Info

    private void loadProfileInfo() {
        try {
            // Reload student data to get latest assignments
            Student updatedStudent = studentDAO.getStudentByStudentId(currentStudent.getStudentId());
            if (updatedStudent != null) {
                currentStudent = updatedStudent;
                SessionManager.getInstance().setCurrentUser(currentStudent);
            }

            // Display assigned bus
            if (currentStudent.getAssignedBusId() != null) {
                Bus bus = busDAO.getBusById(currentStudent.getAssignedBusId());
                if (bus != null) {
                    if (assignedBusLabel != null) {
                        assignedBusLabel.setText(bus.getBusNumber());
                    }
                    if (busCapacityLabel != null) {
                        busCapacityLabel.setText(String.valueOf(bus.getCapacity()));
                    }
                } else {
                    if (assignedBusLabel != null) {
                        assignedBusLabel.setText("Not Assigned");
                    }
                    if (busCapacityLabel != null) {
                        busCapacityLabel.setText("-");
                    }
                }
            } else {
                if (assignedBusLabel != null) {
                    assignedBusLabel.setText("Not Assigned");
                }
                if (busCapacityLabel != null) {
                    busCapacityLabel.setText("-");
                }
            }

            // Display assigned route
            if (currentStudent.getAssignedRouteId() != null) {
                Route route = routeDAO.getRouteById(currentStudent.getAssignedRouteId());
                if (route != null) {
                    if (assignedRouteLabel != null) {
                        assignedRouteLabel.setText(route.getName());
                    }
                    if (routeStartLabel != null) {
                        routeStartLabel.setText(route.getStartPoint());
                    }
                    if (routeEndLabel != null) {
                        routeEndLabel.setText(route.getEndPoint());
                    }
                    if (routeDetailsLabel != null) {
                        routeDetailsLabel.setText(route.getStartPoint() + " → " + route.getEndPoint());
                    }
                } else {
                    if (assignedRouteLabel != null) {
                        assignedRouteLabel.setText("Not Assigned");
                    }
                    if (routeStartLabel != null) {
                        routeStartLabel.setText("-");
                    }
                    if (routeEndLabel != null) {
                        routeEndLabel.setText("-");
                    }
                    if (routeDetailsLabel != null) {
                        routeDetailsLabel.setText("");
                    }
                }
            } else {
                if (assignedRouteLabel != null) {
                    assignedRouteLabel.setText("Not Assigned");
                }
                if (routeStartLabel != null) {
                    routeStartLabel.setText("-");
                }
                if (routeEndLabel != null) {
                    routeEndLabel.setText("-");
                }
                if (routeDetailsLabel != null) {
                    routeDetailsLabel.setText("");
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to load profile info: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Schedules

    private void initializeSchedulesTable() {
        // Only initialize if columns exist
        if (scheduleBusCol != null) {
            scheduleBusCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        }
        if (scheduleRouteCol != null) {
            scheduleRouteCol.setCellValueFactory(new PropertyValueFactory<>("routeName"));
        }
        if (scheduleDateCol != null) {
            scheduleDateCol.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));
        }
        if (scheduleTimeCol != null) {
            scheduleTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduleTime"));
        }
    }

    @FXML
    private void handleRefreshSchedule(ActionEvent event) {
        loadSchedules();
    }

    private void handleRefreshSchedules(ActionEvent event) {
        loadSchedules();
    }

    private void loadSchedules() {
        try {
            ObservableList<Schedule> schedules = scheduleDAO.getStudentSchedules(currentStudent.getId());

            // Only set items if table exists
            if (scheduleTable != null) {
                scheduleTable.setItems(schedules);
            }

            if (schedules.isEmpty()) {
                System.out.println("No schedules found for student: " + currentStudent.getStudentId());
            } else {
                System.out.println("Loaded " + schedules.size() + " schedules");
            }
        } catch (Exception e) {
            System.err.println("Failed to load schedules: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Requests

    private void initializeRequestsTable() {
        // Only initialize columns that exist in FXML
        if (requestIdCol != null) {
            requestIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        }
        if (requestTypeCol != null) {
            requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        if (requestDescCol != null) {
            requestDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        }
        if (requestStatusCol != null) {
            requestStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    @FXML private void handleSubmitRequest(ActionEvent event) {
        // Use FXML form fields directly - no dialog needed
        String type = requestTypeCombo != null ? requestTypeCombo.getValue() : null;
        String description = requestDescriptionArea != null ? requestDescriptionArea.getText().trim() : "";

        // Validation
        if (type == null || type.isEmpty()) {
            showError("Please select a request type!");
            return;
        }

        if (description.isEmpty()) {
            showError("Please enter a description!");
            return;
        }

        try {
            // Submit request
            requestDAO.addRequest(currentStudent.getStudentId(), type, description);

            // Log action
            Logger.getInstance().studentAction(currentStudent.getUsername(),
                    "Submitted request: " + type);

            // Clear form
            if (requestTypeCombo != null) {
                requestTypeCombo.setValue(null);
            }
            if (requestDescriptionArea != null) {
                requestDescriptionArea.clear();
            }

            // Show success and refresh
            showSuccess("Request submitted successfully! Admin will review it.");
            loadRequests();

        } catch (Exception e) {
            showError("Failed to submit request: " + e.getMessage());
        }
    }

    @FXML private void handleRefreshRequests(ActionEvent event) {
        loadRequests();
    }

    private void loadRequests() {
        try {
            ObservableList<Request> requests = requestDAO.getRequestsByStudentId(currentStudent.getStudentId());
            requestsTable.setItems(requests);
        } catch (Exception e) {
            showError("Failed to load requests: " + e.getMessage());
        }
    }

    //Messages

    private void initializeMessagesTable() {
        // Only initialize columns that exist in FXML
        if (messageDateCol != null) {
            messageDateCol.setCellValueFactory(new PropertyValueFactory<>("messageDate"));
        }
        if (messageContentCol != null) {
            messageContentCol.setCellValueFactory(new PropertyValueFactory<>("message"));

            // Make message content column wrap text
            messageContentCol.setCellFactory(tc -> {
                TableCell<Message, String> cell = new TableCell<>();
                javafx.scene.text.Text text = new javafx.scene.text.Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(messageContentCol.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            });
        }
    }

    @FXML private void handleRefreshMessages(ActionEvent event) {
        loadMessages();
    }

    private void loadMessages() {
        try {
            ObservableList<Message> messages = messageDAO.getMessagesByUserId(currentStudent.getId());
            messagesTable.setItems(messages);

            // Update unread count
            int unreadCount = messageDAO.getUnreadMessageCount(currentStudent.getId());
            unreadCountLabel.setText("Unread: " + unreadCount);

            // Mark messages as read when viewed
            if (!messages.isEmpty()) {
                for (Message msg : messages) {
                    if (!msg.isRead()) {
                        messageDAO.markAsRead(msg.getMessageId());
                    }
                }
            }
        } catch (Exception e) {
            showError("Failed to load messages: " + e.getMessage());
        }
    }

    private void startMessageRefreshThread() {
        messageRefreshThread = new Thread(() -> {
            while (keepRefreshing) {
                try {
                    Thread.sleep(10000); // Sleep for 10 seconds

                    // Update UI on JavaFX Application Thread
                    Platform.runLater(() -> {
                        try {
                            int unreadCount = messageDAO.getUnreadMessageCount(currentStudent.getId());
                            if (unreadCount > 0) {
                                unreadCountLabel.setText("Unread: " + unreadCount + " (New!)");
                                unreadCountLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            } else {
                                unreadCountLabel.setText("Unread: 0");
                                unreadCountLabel.setStyle("-fx-text-fill: black;");
                            }
                        } catch (Exception e) {
                            // Silent fail for background refresh
                        }
                    });
                } catch (InterruptedException e) {
                    break; // Exit thread if interrupted
                }
            }
        });

        messageRefreshThread.setDaemon(true); // Make it a daemon thread
        messageRefreshThread.start();
        Logger.getInstance().info("Message auto-refresh thread started for student: " + currentStudent.getStudentId());
    }
    
    private void stopMessageRefreshThread() {
        keepRefreshing = false;
        if (messageRefreshThread != null) {
            messageRefreshThread.interrupt();
        }
        Logger.getInstance().info("Message auto-refresh thread stopped for student: " + currentStudent.getStudentId());
    }

    //Lggout

    @FXML private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Stop message refresh thread
                stopMessageRefreshThread();

                Logger.getInstance().studentAction(currentStudent.getUsername(), "Logged out");
                SessionManager.getInstance().logout();
                SceneManager.switchScene("/com/example/kuet_transportation_and_schedueling_system/view/Login.fxml",
                        "Login");
            } catch (IOException e) {
                showError("Failed to logout: " + e.getMessage());
            }
        }
    }

    //Utility Methods

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void cleanup() {
        stopMessageRefreshThread();
    }
}