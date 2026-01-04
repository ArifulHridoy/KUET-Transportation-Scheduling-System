package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.*;

public class Route {
    private int routeId;
    private String name;
    private String startPoint;
    private String endPoint;

    // JavaFX Properties
    private IntegerProperty routeIdProperty;
    private StringProperty nameProperty;
    private StringProperty startPointProperty;
    private StringProperty endPointProperty;

    public Route() {}

    public Route(int routeId, String name, String startPoint, String endPoint) {
        this.routeId = routeId;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        initProperties();
    }

    private void initProperties() {
        this.routeIdProperty = new SimpleIntegerProperty(routeId);
        this.nameProperty = new SimpleStringProperty(name);
        this.startPointProperty = new SimpleStringProperty(startPoint);
        this.endPointProperty = new SimpleStringProperty(endPoint);
    }

    // Getters and Setters
    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
        if (routeIdProperty != null) {
            routeIdProperty.set(routeId);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (nameProperty != null) {
            nameProperty.set(name);
        }
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
        if (startPointProperty != null) {
            startPointProperty.set(startPoint);
        }
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        if (endPointProperty != null) {
            endPointProperty.set(endPoint);
        }
    }

    // JavaFX Properties
    public IntegerProperty routeIdProperty() {
        if (routeIdProperty == null) {
            routeIdProperty = new SimpleIntegerProperty(routeId);
        }
        return routeIdProperty;
    }

    public StringProperty nameProperty() {
        if (nameProperty == null) {
            nameProperty = new SimpleStringProperty(name);
        }
        return nameProperty;
    }

    public StringProperty startPointProperty() {
        if (startPointProperty == null) {
            startPointProperty = new SimpleStringProperty(startPoint);
        }
        return startPointProperty;
    }

    public StringProperty endPointProperty() {
        if (endPointProperty == null) {
            endPointProperty = new SimpleStringProperty(endPoint);
        }
        return endPointProperty;
    }

    @Override public String toString() {
        return name + " (" + startPoint + " → " + endPoint + ")";
    }
}

