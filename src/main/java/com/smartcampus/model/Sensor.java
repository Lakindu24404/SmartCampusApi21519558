package com.smartcampus.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Represents a sensor deployed within a Smart Campus room.
 * Status values: "ACTIVE", "MAINTENANCE", "OFFLINE"
 */
@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @Column(length = 20)
    @NotBlank(message = "Sensor ID is required")
    private String id;            // Unique identifier, e.g. "TEMP-001"

    @NotBlank(message = "Sensor type is required")
    private String type;          // Category: "Temperature", "Occupancy", "CO2"

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|MAINTENANCE|OFFLINE", message = "Status must be ACTIVE, MAINTENANCE, or OFFLINE")
    private String status;        // Current state: "ACTIVE", "MAINTENANCE", "OFFLINE"

    private double currentValue;  // Most recent measurement recorded

    @NotBlank(message = "Room ID is required")
    private String roomId;        // Foreign key linking to the Room

    // ── Constructors ────────────────────────────────────────────────────────────

    public Sensor() {}

    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
