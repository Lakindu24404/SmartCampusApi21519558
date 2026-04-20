package com.smartcampus.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Represents a historical sensor reading event.
 * Annotated for JPA persistence.
 */
@Entity
@Table(name = "sensor_readings")
public class SensorReading {

    @Id
    private String id;        // Unique reading event ID (UUID)

    @NotNull(message = "Timestamp is required")
    private long timestamp;   // Epoch time (ms) when the reading was captured

    private double value;     // The actual metric value recorded by the hardware

    @NotBlank(message = "Sensor ID is required")
    private String sensorId;  // Foreign key linking to the Sensor

    // ── Constructors ────────────────────────────────────────────────────────────

    public SensorReading() {}

    public SensorReading(String id, long timestamp, double value, String sensorId) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.sensorId = sensorId;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}
