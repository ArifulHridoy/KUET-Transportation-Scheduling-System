package com.example.kuet_transportation_and_schedueling_system.model;

public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int id, String username, String password) {
        super(id, username, password, "admin");
    }

    @Override public void displayDashboard() {
        System.out.println("Loading Admin Dashboard for: " + getUsername());
    }

    public void manageRoutes() {
        System.out.println("Admin managing routes...");
    }

    public void manageBuses() {
        System.out.println("Admin managing buses...");
    }

    public void manageSchedules() {
        System.out.println("Admin managing schedules...");
    }

    public void viewRequests() {
        System.out.println("Admin viewing student requests...");
    }

    public void sendNotifications() {
        System.out.println("Admin sending notifications...");
    }
}

