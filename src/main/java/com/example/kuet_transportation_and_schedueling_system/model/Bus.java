package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.*;

public class Bus {
    private int busId;
    private String busNumber;
    private int capacity;

    // JavaFX Properties
    private IntegerProperty busIdProperty;
    private StringProperty busNumberProperty;
    private IntegerProperty capacityProperty;

    public Bus() {}

    public Bus(int busId, String busNumber, int capacity) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.capacity = capacity;
        initProperties();
    }

    private void initProperties() {
        this.busIdProperty = new SimpleIntegerProperty(busId);
        this.busNumberProperty = new SimpleStringProperty(busNumber);
        this.capacityProperty = new SimpleIntegerProperty(capacity);
    }

    // Getters and Setters
    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
        if (busIdProperty != null) {
            busIdProperty.set(busId);
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if (capacityProperty != null) {
            capacityProperty.set(capacity);
        }
    }

    // JavaFX Properties
    public IntegerProperty busIdProperty() {
        if (busIdProperty == null) {
            busIdProperty = new SimpleIntegerProperty(busId);
        }
        return busIdProperty;
    }

    public StringProperty busNumberProperty() {
        if (busNumberProperty == null) {
            busNumberProperty = new SimpleStringProperty(busNumber);
        }
        return busNumberProperty;
    }

    public IntegerProperty capacityProperty() {
        if (capacityProperty == null) {
            capacityProperty = new SimpleIntegerProperty(capacity);
        }
        return capacityProperty;
    }

    @Override public String toString() {
        return "Bus " + busNumber + " (Capacity: " + capacity + ")";
    }
}

