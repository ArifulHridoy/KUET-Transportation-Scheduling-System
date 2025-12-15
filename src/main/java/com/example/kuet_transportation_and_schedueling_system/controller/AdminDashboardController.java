package com.example.kuet_transportation_and_schedueling_system.controller;

import com.example.kuet_transportation_and_schedueling_system.dao.*;
import com.example.kuet_transportation_and_schedueling_system.model.Route;
import com.example.kuet_transportation_and_schedueling_system.model.Bus;
import com.example.kuet_transportation_and_schedueling_system.model.Schedule;
import com.example.kuet_transportation_and_schedueling_system.model.Student;
import com.example.kuet_transportation_and_schedueling_system.model.Request;
import com.example.kuet_transportation_and_schedueling_system.model.RequestStatus;
import com.example.kuet_transportation_and_schedueling_system.model.User;
import com.example.kuet_transportation_and_schedueling_system.util.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class AdminDashboardController {

    // Tab references
    @FXML private TabPane mainTabPane;
    @FXML private Tab routesTab;
    @FXML private Tab busesTab;
    @FXML private Tab schedulesTab;
    @FXML private Tab studentsTab;
    @FXML private Tab requestsTab;
    @FXML private Tab messagesTab;

    // Routes Tab
    @FXML private TableView<Route> routesTable;
    @FXML private TableColumn<Route, Integer> routeIdCol;
    @FXML private TableColumn<Route, String> routeNameCol;
    @FXML private TableColumn<Route, String> routeStartCol;
    @FXML private TableColumn<Route, String> routeEndCol;
    @FXML private Button addRouteBtn;
    @FXML private Button editRouteBtn;
    @FXML private Button deleteRouteBtn;
    @FXML private Button refreshRoutesBtn;

    // Buses Tab
    @FXML private TableView<Bus> busesTable;
    @FXML private TableColumn<Bus, Integer> busIdCol;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, Integer> busCapacityCol;
    @FXML private Button addBusBtn;
    @FXML private Button editBusBtn;
    @FXML private Button deleteBusBtn;
    @FXML private Button refreshBusesBtn;

    // Schedules Tab
    @FXML private TableView<Schedule> schedulesTable;
    @FXML private TableColumn<Schedule, Integer> scheduleIdCol;
    @FXML private TableColumn<Schedule, String> scheduleBusCol;
    @FXML private TableColumn<Schedule, String> scheduleRouteCol;
    @FXML private TableColumn<Schedule, LocalDate> scheduleDateCol;
    @FXML private TableColumn<Schedule, LocalTime> scheduleTimeCol;
    @FXML private Button addScheduleBtn;
    @FXML private Button deleteScheduleBtn;
    @FXML private Button refreshSchedulesBtn;

    // Students Tab
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> studentIdCol;
    @FXML private TableColumn<Student, String> studentNameCol;
    @FXML private TableColumn<Student, String> studentDeptCol;
    @FXML private TableColumn<Student, String> studentRouteCol;
    @FXML private TableColumn<Student, String> studentBusCol;
    @FXML private Button assignStudentBtn;
    @FXML private Button removeAssignmentBtn;
    @FXML private Button refreshStudentsBtn;

    // Requests Tab
    @FXML private TableView<Request> requestsTable;
    @FXML private TableColumn<Request, Integer> requestIdCol;
    @FXML private TableColumn<Request, String> requestStudentIdCol;
    @FXML private TableColumn<Request, String> requestStudentNameCol;
    @FXML private TableColumn<Request, String> requestStudentCol;
    @FXML private TableColumn<Request, String> requestTypeCol;
    @FXML private TableColumn<Request, String> requestDescCol;
    @FXML private TableColumn<Request, RequestStatus> requestStatusCol;
    @FXML private TableColumn<Request, String> requestDateCol;
    @FXML private Button approveRequestBtn;
    @FXML private Button rejectRequestBtn;
    @FXML private Button refreshRequestsBtn;

    // Messages Tab
    @FXML private TextArea messageTextArea;
    @FXML private Button sendMessageBtn;
    @FXML private Button sendToAllBtn;
    @FXML private Button sendToSelectedBtn;

    // Top bar
    @FXML private Label welcomeLabel;
    @FXML private Button logoutBtn;

    // Dashboard Statistics
    @FXML private Label totalRoutesLabel;
    @FXML private Label totalBusesLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label pendingRequestsLabel;

    // DAOs
    private final RouteDAO routeDAO = new RouteDAO();
    private final BusDAO busDAO = new BusDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final MessageDAO messageDAO = new MessageDAO();

    @FXML
    public void initialize() {
        // Set welcome message
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, Admin: " + currentUser.getUsername());
        }
        
        initializeRoutesTable();
        initializeBusesTable();
        initializeSchedulesTable();
        initializeStudentsTable();
        initializeRequestsTable();
        
        loadAllData();
        
        loadDashboardStats();
    }
    
    private void loadDashboardStats() {
        try {
            if (totalRoutesLabel != null) {
                int routeCount = routeDAO.getAllRoutes().size();
                totalRoutesLabel.setText(String.valueOf(routeCount));
            }

            if (totalBusesLabel != null) {
                int busCount = busDAO.getAllBuses().size();
                totalBusesLabel.setText(String.valueOf(busCount));
            }

            if (totalStudentsLabel != null) {
                int studentCount = studentDAO.getAllStudents().size();
                totalStudentsLabel.setText(String.valueOf(studentCount));
            }

            if (pendingRequestsLabel != null) {
                int pendingCount = (int) requestDAO.getAllRequests().stream()
                    .filter(r -> r.getStatus() == RequestStatus.PENDING)
                    .count();
                pendingRequestsLabel.setText(String.valueOf(pendingCount));
            }
        } catch (Exception e) {
            System.err.println("Error loading dashboard stats: " + e.getMessage());
        }
    }

    private void initializeRoutesTable() {
        routeIdCol.setCellValueFactory(new PropertyValueFactory<>("routeId"));
        routeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        routeStartCol.setCellValueFactory(new PropertyValueFactory<>("startPoint"));
        routeEndCol.setCellValueFactory(new PropertyValueFactory<>("endPoint"));
    }

    @FXML private void handleAddRoute(ActionEvent event) {
        Dialog<Route> dialog = new Dialog<>();
        dialog.setTitle("Add New Route");
        dialog.setHeaderText("Enter route details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Route Name");
        TextField startField = new TextField();
        startField.setPromptText("Start Point");
        TextField endField = new TextField();
        endField.setPromptText("End Point");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Start:"), 0, 1);
        grid.add(startField, 1, 1);
        grid.add(new Label("End:"), 0, 2);
        grid.add(endField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    routeDAO.addRoute(nameField.getText(), startField.getText(), endField.getText());
                    Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                            "Added route: " + nameField.getText());
                    loadRoutes();
                    showSuccess("Route added successfully!");
                } catch (Exception e) {
                    showError("Failed to add route: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML private void handleEditRoute(ActionEvent event) {
        Route selected = routesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a route to edit!");
            return;
        }

        Dialog<Route> dialog = new Dialog<>();
        dialog.setTitle("Edit Route");
        dialog.setHeaderText("Update route details");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(selected.getName());
        TextField startField = new TextField(selected.getStartPoint());
        TextField endField = new TextField(selected.getEndPoint());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Start:"), 0, 1);
        grid.add(startField, 1, 1);
        grid.add(new Label("End:"), 0, 2);
        grid.add(endField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    routeDAO.updateRoute(selected.getRouteId(), nameField.getText(),
                            startField.getText(), endField.getText());
                    Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                            "Updated route: " + selected.getRouteId());
                    loadRoutes();
                    showSuccess("Route updated successfully!");
                } catch (Exception e) {
                    showError("Failed to update route: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleDeleteRoute(ActionEvent event) {
        Route selected = routesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a route to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Route");
        alert.setContentText("Are you sure you want to delete: " + selected.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                routeDAO.deleteRoute(selected.getRouteId());
                Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                        "Deleted route: " + selected.getRouteId());
                loadRoutes();
                showSuccess("Route deleted successfully!");
            } catch (Exception e) {
                showError("Failed to delete route: " + e.getMessage());
            }
        }
    }

    @FXML private void handleRefreshRoutes(ActionEvent event) {
        loadRoutes();
    }

    private void loadRoutes() {
        try {
            ObservableList<Route> routes = routeDAO.getAllRoutes();
            routesTable.setItems(routes);
        } catch (Exception e) {
            showError("Failed to load routes: " + e.getMessage());
        }
    }

    private void initializeBusesTable() {
        busIdCol.setCellValueFactory(new PropertyValueFactory<>("busId"));
        busNumberCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        busCapacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    }

    @FXML private void handleAddBus(ActionEvent event) {
        Dialog<Bus> dialog = new Dialog<>();
        dialog.setTitle("Add New Bus");
        dialog.setHeaderText("Enter bus details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField numberField = new TextField();
        numberField.setPromptText("Bus Number (e.g., KUET-01)");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        grid.add(new Label("Bus Number:"), 0, 0);
        grid.add(numberField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capacityField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int capacity = Integer.parseInt(capacityField.getText());
                    if (!Validator.isValidCapacity(capacity)) {
                        showError("Capacity must be between 1 and 100!");
                        return null;
                    }
                    busDAO.addBus(numberField.getText(), capacity);
                    Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                            "Added bus: " + numberField.getText());
                    loadBuses();
                    showSuccess("Bus added successfully!");
                } catch (NumberFormatException e) {
                    showError("Invalid capacity! Please enter a number.");
                } catch (Exception e) {
                    showError("Failed to add bus: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML private void handleEditBus(ActionEvent event) {
        Bus selected = busesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a bus to edit!");
            return;
        }

        Dialog<Bus> dialog = new Dialog<>();
        dialog.setTitle("Edit Bus");
        dialog.setHeaderText("Update bus details");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField numberField = new TextField(selected.getBusNumber());
        TextField capacityField = new TextField(String.valueOf(selected.getCapacity()));

        grid.add(new Label("Bus Number:"), 0, 0);
        grid.add(numberField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capacityField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    int capacity = Integer.parseInt(capacityField.getText());
                    busDAO.updateBus(selected.getBusId(), numberField.getText(), capacity);
                    Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                            "Updated bus: " + selected.getBusId());
                    loadBuses();
                    showSuccess("Bus updated successfully!");
                } catch (Exception e) {
                    showError("Failed to update bus: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML private void handleDeleteBus(ActionEvent event) {
        Bus selected = busesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a bus to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Bus");
        alert.setContentText("Are you sure you want to delete: " + selected.getBusNumber() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                busDAO.deleteBus(selected.getBusId());
                Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                        "Deleted bus: " + selected.getBusId());
                loadBuses();
                showSuccess("Bus deleted successfully!");
            } catch (Exception e) {
                showError("Failed to delete bus: " + e.getMessage());
            }
        }
    }

    @FXML private void handleRefreshBuses(ActionEvent event) {
        loadBuses();
    }

    private void loadBuses() {
        try {
            ObservableList<Bus> buses = busDAO.getAllBuses();
            busesTable.setItems(buses);
        } catch (Exception e) {
            showError("Failed to load buses: " + e.getMessage());
        }
    }

    private void initializeSchedulesTable() {
        scheduleIdCol.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        scheduleBusCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        scheduleRouteCol.setCellValueFactory(new PropertyValueFactory<>("routeName"));
        scheduleDateCol.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));
        scheduleTimeCol.setCellValueFactory(new PropertyValueFactory<>("scheduleTime"));
    }

    @FXML private void handleAddSchedule(ActionEvent event) {
        try {
            ObservableList<Bus> buses = busDAO.getAllBuses();
            ObservableList<Route> routes = routeDAO.getAllRoutes();

            if (buses.isEmpty() || routes.isEmpty()) {
                showError("Please add buses and routes first!");
                return;
            }

            Dialog<Schedule> dialog = new Dialog<>();
            dialog.setTitle("Add New Schedule");
            dialog.setHeaderText("Create bus schedule");

            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            ComboBox<Bus> busCombo = new ComboBox<>(buses);
            ComboBox<Route> routeCombo = new ComboBox<>(routes);
            DatePicker datePicker = new DatePicker(LocalDate.now());
            TextField timeField = new TextField("08:00");
            timeField.setPromptText("HH:MM format");

            grid.add(new Label("Bus:"), 0, 0);
            grid.add(busCombo, 1, 0);
            grid.add(new Label("Route:"), 0, 1);
            grid.add(routeCombo, 1, 1);
            grid.add(new Label("Date:"), 0, 2);
            grid.add(datePicker, 1, 2);
            grid.add(new Label("Time:"), 0, 3);
            grid.add(timeField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    try {
                        Bus selectedBus = busCombo.getValue();
                        Route selectedRoute = routeCombo.getValue();
                        LocalDate date = datePicker.getValue();
                        LocalTime time = LocalTime.parse(timeField.getText());

                        if (selectedBus == null || selectedRoute == null) {
                            showError("Please select bus and route!");
                            return null;
                        }

                        scheduleDAO.addSchedule(selectedBus.getBusId(), selectedRoute.getRouteId(), date, time);
                        Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                                "Created schedule for bus " + selectedBus.getBusNumber());
                        loadSchedules();
                        showSuccess("Schedule added successfully!");
                    } catch (Exception e) {
                        showError("Failed to add schedule: " + e.getMessage());
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } catch (Exception e) {
            showError("Failed to load data: " + e.getMessage());
        }
    }

    @FXML private void handleDeleteSchedule(ActionEvent event) {
        Schedule selected = schedulesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a schedule to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Schedule");
        alert.setContentText("Delete schedule for " + selected.getBusNumber() + " on " + selected.getScheduleDate() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                scheduleDAO.deleteSchedule(selected.getScheduleId());
                Logger.getInstance().adminAction(SessionManager.getInstance().getCurrentUser().getUsername(),
                        "Deleted schedule: " + selected.getScheduleId());
                loadSchedules();
                showSuccess("Schedule deleted successfully!");
            } catch (Exception e) {
                showError("Failed to delete schedule: " + e.getMessage());
            }
        }
    }

    @FXML private void handleRefreshSchedules(ActionEvent event) {
        loadSchedules();
    }

    private void loadSchedules() {
        try {
            ObservableList<Schedule> schedules = scheduleDAO.getAllSchedules();
            schedulesTable.setItems(schedules);
        } catch (Exception e) {
            showError("Failed to load schedules: " + e.getMessage());
        }
    }