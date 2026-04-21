// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
public class SmartSensor {
    private String id;
    private String type;
    private String status;
    private double currentValue;
    private String roomId;
    public SmartSensor() {
    }
    public SmartSensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }
    // this works better
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
    // just what i need
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
    // setting this up
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
