// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class Room { // physical campus room

    @NotBlank(message = "Room ID is required")
    private String id;

    @NotBlank(message = "Room name is required")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity; // max people allowed

    private List<String> sensorIds = new ArrayList<>(); // list of sensors in this room

    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }

    public void addSensorId(String sensorId) {
        if (!this.sensorIds.contains(sensorId)) {
            this.sensorIds.add(sensorId);
        }
    }

    public void removeSensorId(String sensorId) {
        this.sensorIds.remove(sensorId);
    }
}
