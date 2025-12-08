package com.example.trscsy.model;

public class Driver {
    private Long id;
    private String name;
    private String phoneNumber;
    private String licenseNumber;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE
    private Long assignedBusId;
    private String assignedBusNumber;

    public Driver() {}

    public Driver(Long id, String name, String phoneNumber, String licenseNumber, String status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAssignedBusId() { return assignedBusId; }
    public void setAssignedBusId(Long assignedBusId) { this.assignedBusId = assignedBusId; }

    public String getAssignedBusNumber() { return assignedBusNumber; }
    public void setAssignedBusNumber(String assignedBusNumber) { this.assignedBusNumber = assignedBusNumber; }
}
