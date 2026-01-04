package com.example.kuet_transportation_and_schedueling_system.model;

public enum RequestStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override public String toString() {
        return displayName;
    }
}

