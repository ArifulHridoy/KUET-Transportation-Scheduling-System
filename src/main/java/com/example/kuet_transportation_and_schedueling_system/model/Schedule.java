package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private int scheduleId;
    private int busId;
    private int routeId;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;

    // Additional fields for display
    private String busNumber;
    private String routeName;

    // JavaFX Properties
    private IntegerProperty scheduleIdProperty;
    private StringProperty busNumberProperty;
    private StringProperty routeNameProperty;
    private ObjectProperty<LocalDate> scheduleDateProperty;
    private ObjectProperty<LocalTime> scheduleTimeProperty;

    public Schedule() {}

    public Schedule(int scheduleId, int busId, int routeId, LocalDate scheduleDate, LocalTime scheduleTime) {
        this.scheduleId = scheduleId;
        this.busId = busId;
        this.routeId = routeId;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        initProperties();
    }

    private void initProperties() {
        this.scheduleIdProperty = new SimpleIntegerProperty(scheduleId);
        this.busNumberProperty = new SimpleStringProperty(busNumber);
        this.routeNameProperty = new SimpleStringProperty(routeName);
        this.scheduleDateProperty = new SimpleObjectProperty<>(scheduleDate);
        this.scheduleTimeProperty = new SimpleObjectProperty<>(scheduleTime);
    }

    // Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
        if (scheduleIdProperty != null) {
            scheduleIdProperty.set(scheduleId);
        }
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
        if (scheduleDateProperty != null) {
            scheduleDateProperty.set(scheduleDate);
        }
    }

    public LocalTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalTime scheduleTime) {
        this.scheduleTime = scheduleTime;
        if (scheduleTimeProperty != null) {
            scheduleTimeProperty.set(scheduleTime);
        }
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
        if (busNumberProperty != null) {
            busNumberProperty.set(busNumber);
        }
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
        if (routeNameProperty != null) {
            routeNameProperty.set(routeName);
        }
    }

    // JavaFX Properties
    public IntegerProperty scheduleIdProperty() {
        if (scheduleIdProperty == null) {
            scheduleIdProperty = new SimpleIntegerProperty(scheduleId);
        }
        return scheduleIdProperty;
    }

    public StringProperty busNumberProperty() {
        if (busNumberProperty == null) {
            busNumberProperty = new SimpleStringProperty(busNumber);
        }
        return busNumberProperty;
    }

    public StringProperty routeNameProperty() {
        if (routeNameProperty == null) {
            routeNameProperty = new SimpleStringProperty(routeName);
        }
        return routeNameProperty;
    }

    public ObjectProperty<LocalDate> scheduleDateProperty() {
        if (scheduleDateProperty == null) {
            scheduleDateProperty = new SimpleObjectProperty<>(scheduleDate);
        }
        return scheduleDateProperty;
    }

    public ObjectProperty<LocalTime> scheduleTimeProperty() {
        if (scheduleTimeProperty == null) {
            scheduleTimeProperty = new SimpleObjectProperty<>(scheduleTime);
        }
        return scheduleTimeProperty;
    }

    @Override public String toString() {
        return "Schedule{" +
                "date=" + scheduleDate +
                ", time=" + scheduleTime +
                ", bus=" + busNumber +
                ", route=" + routeName +
                '}';
    }
}

