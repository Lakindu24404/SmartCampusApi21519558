// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
    // main class here
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
    // return id
    public String getId() {
        return id;
    }
    // set id value
    public void setId(String id) {
        this.id = id;
    }
    // standard getter
    public long getTimestamp() {
        return timestamp;
    }
    // setting data
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    // standard getter
    public double getValue() {
        return value;
    }
    // setting data
    public void setValue(double value) {
        this.value = value;
    }
}
