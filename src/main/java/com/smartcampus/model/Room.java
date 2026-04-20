package com.smartcampus.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical room in the Smart Campus.
 * Annotated for JPA persistence and Bean Validation.
 */
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(length = 20)
    @NotBlank(message = "Room ID is required")
    private String id;           // Unique identifier, e.g. "LIB-301"

    @NotBlank(message = "Room name is required")
    @Column(nullable = false)
    private String name;         // Human-readable name, e.g. "Library Quiet Study"

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;        // Maximum occupancy for safety regulations

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_sensors", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "sensor_id")
    private List<String> sensorIds = new ArrayList<>();  // IDs of sensors in this room

    // ── Constructors ────────────────────────────────────────────────────────────

    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }

    /**
     * Convenience method to add a sensor ID to this room.
     */
    public void addSensorId(String sensorId) {
        if (!this.sensorIds.contains(sensorId)) {
            this.sensorIds.add(sensorId);
        }
    }

    /**
     * Convenience method to remove a sensor ID from this room.
     */
    public void removeSensorId(String sensorId) {
        this.sensorIds.remove(sensorId);
    }
}
