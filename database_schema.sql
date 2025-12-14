-- KUET Transportation and Scheduling System Database Schema

CREATE DATABASE IF NOT EXISTS kuet_transport;
USE kuet_transport;

-- Users table (for both admin and students)
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    user_id INT UNIQUE,
    assigned_route_id INT NULL,
    assigned_bus_id INT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Routes table
CREATE TABLE IF NOT EXISTS routes (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    start_point VARCHAR(100) NOT NULL,
    end_point VARCHAR(100) NOT NULL
);

-- Buses table
CREATE TABLE IF NOT EXISTS buses (
    bus_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_number VARCHAR(20) UNIQUE NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0)
);

-- Schedules table
CREATE TABLE IF NOT EXISTS schedules (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_id INT NOT NULL,
    route_id INT NOT NULL,
    schedule_date DATE NOT NULL,
    schedule_time TIME NOT NULL,
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);

-- Requests table
CREATE TABLE IF NOT EXISTS requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Messages table (for notifications)
CREATE TABLE IF NOT EXISTS messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    message_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Add foreign keys to students table (if not already added)
ALTER TABLE students
ADD FOREIGN KEY (assigned_route_id) REFERENCES routes(route_id) ON DELETE SET NULL,
ADD FOREIGN KEY (assigned_bus_id) REFERENCES buses(bus_id) ON DELETE SET NULL;

-- Insert default admin
INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE username=username;

-- Sample data for testing
INSERT INTO routes (name, start_point, end_point) VALUES
('Route 1', 'KUET Gate', 'Khulna City Center'),
('Route 2', 'KUET Gate', 'Sonadanga Bus Stand'),
('Route 3', 'KUET Gate', 'Railway Station')
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO buses (bus_number, capacity) VALUES
('KUET-01', 40),
('KUET-02', 35),
('KUET-03', 45)
ON DUPLICATE KEY UPDATE bus_number=bus_number;

-- Sample schedules
INSERT INTO schedules (bus_id, route_id, schedule_date, schedule_time) VALUES
(1, 1, CURDATE(), '08:00:00'),
(1, 1, CURDATE(), '14:00:00'),
(2, 2, CURDATE(), '09:00:00'),
(2, 2, CURDATE(), '15:00:00'),
(3, 3, CURDATE(), '10:00:00'),
(3, 3, CURDATE(), '16:00:00')
ON DUPLICATE KEY UPDATE schedule_id=schedule_id;

COMMIT;

