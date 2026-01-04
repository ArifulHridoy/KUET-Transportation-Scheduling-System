package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student extends User {
    private String studentId;
    private String name;
    private String department;
    private Integer assignedRouteId;
    private Integer assignedBusId;

    // JavaFX Properties for TableView binding
    private StringProperty studentIdProperty;
    private StringProperty nameProperty;
    private StringProperty departmentProperty;

    public Student() {
        super();
    }

    public Student(int id, String username, String password, String studentId, String name, String department) {
        super(id, username, password, "student");
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        initProperties();
    }

    private void initProperties() {
        this.studentIdProperty = new SimpleStringProperty(studentId);
        this.nameProperty = new SimpleStringProperty(name);
        this.departmentProperty = new SimpleStringProperty(department);
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
        if (studentIdProperty != null) {
            studentIdProperty.set(studentId);
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        if (departmentProperty != null) {
            departmentProperty.set(department);
        }
    }

    public Integer getAssignedRouteId() {
        return assignedRouteId;
    }

    public void setAssignedRouteId(Integer assignedRouteId) {
        this.assignedRouteId = assignedRouteId;
    }

    public Integer getAssignedBusId() {
        return assignedBusId;
    }

    public void setAssignedBusId(Integer assignedBusId) {
        this.assignedBusId = assignedBusId;
    }

    // JavaFX Properties for TableView
    public StringProperty studentIdProperty() {
        if (studentIdProperty == null) {
            studentIdProperty = new SimpleStringProperty(studentId);
        }
        return studentIdProperty;
    }

    public StringProperty nameProperty() {
        if (nameProperty == null) {
            nameProperty = new SimpleStringProperty(name);
        }
        return nameProperty;
    }

    public StringProperty departmentProperty() {
        if (departmentProperty == null) {
            departmentProperty = new SimpleStringProperty(department);
        }
        return departmentProperty;
    }

    @Override public void displayDashboard() {
        System.out.println("Loading Student Dashboard for: " + name);
    }

    @Override public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}

