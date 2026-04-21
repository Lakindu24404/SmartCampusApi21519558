// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Sensor { // campus iot sensor

    @NotBlank(message = "Sensor ID is required")
    private String id;

    @NotBlank(message = "Sensor type is required")
    private String type; // temp, co2, etc

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|MAINTENANCE|OFFLINE", message = "Status must be ACTIVE, MAINTENANCE, or OFFLINE")
    private String status;

    private double currentValue; // last reading

    @NotBlank(message = "Room ID is required")
    private String roomId; // where it is located

    public Sensor() {}

    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}
