// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
public class SensorDataReading {
    private String id;
    private long timestamp;
    private double value;
    public SensorDataReading() {
    }
    public SensorDataReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }
    // this works better
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
}
