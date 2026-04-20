// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sensor_readings")
public class SensorReading { // log of sensor readings

    @Id
    private String id; 

    @NotNull(message = "Timestamp is required")
    private long timestamp; 

    private double value; 

    @NotBlank(message = "Sensor ID is required")
    private String sensorId; // which sensor recorded this

    public SensorReading() {}

    public SensorReading(String id, long timestamp, double value, String sensorId) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.sensorId = sensorId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
}
