package com.example.kuet_transportation_and_schedueling_system.model;
import javafx.beans.property.*;

public class Request {
    private int requestId;
    private String studentId;
    private String studentName;
    private String type;
    private String description;
    private RequestStatus status;

    // JavaFX Properties
    private IntegerProperty requestIdProperty;
    private StringProperty studentIdProperty;
    private StringProperty studentNameProperty;
    private StringProperty typeProperty;
    private StringProperty descriptionProperty;
    private ObjectProperty<RequestStatus> statusProperty;

    public Request() {
        this.status = RequestStatus.PENDING;
    }

    public Request(int requestId, String studentId, String studentName, String type, String description, RequestStatus status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.type = type;
        this.description = description;
        this.status = status != null ? status : RequestStatus.PENDING;
        initProperties();
    }

    private void initProperties() {
        this.requestIdProperty = new SimpleIntegerProperty(requestId);
        this.studentIdProperty = new SimpleStringProperty(studentId);
        this.studentNameProperty = new SimpleStringProperty(studentName);
        this.typeProperty = new SimpleStringProperty(type);
        this.descriptionProperty = new SimpleStringProperty(description);
        this.statusProperty = new SimpleObjectProperty<>(status);
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
        if (requestIdProperty != null) {
            requestIdProperty.set(requestId);
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
        if (studentIdProperty != null) {
            studentIdProperty.set(studentId);
        }
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
        if (studentNameProperty != null) {
            studentNameProperty.set(studentName);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (typeProperty != null) {
            typeProperty.set(type);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (descriptionProperty != null) {
            descriptionProperty.set(description);
        }
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
        if (statusProperty != null) {
            statusProperty.set(status);
        }
    }

    // JavaFX Properties
    public IntegerProperty requestIdProperty() {
        if (requestIdProperty == null) {
            requestIdProperty = new SimpleIntegerProperty(requestId);
        }
        return requestIdProperty;
    }

    public StringProperty studentIdProperty() {
        if (studentIdProperty == null) {
            studentIdProperty = new SimpleStringProperty(studentId);
        }
        return studentIdProperty;
    }

    public StringProperty studentNameProperty() {
        if (studentNameProperty == null) {
            studentNameProperty = new SimpleStringProperty(studentName);
        }
        return studentNameProperty;
    }

    public StringProperty typeProperty() {
        if (typeProperty == null) {
            typeProperty = new SimpleStringProperty(type);
        }
        return typeProperty;
    }

    public StringProperty descriptionProperty() {
        if (descriptionProperty == null) {
            descriptionProperty = new SimpleStringProperty(description);
        }
        return descriptionProperty;
    }

    public ObjectProperty<RequestStatus> statusProperty() {
        if (statusProperty == null) {
            statusProperty = new SimpleObjectProperty<>(status);
        }
        return statusProperty;
    }

    @Override public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", studentId='" + studentId + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}

