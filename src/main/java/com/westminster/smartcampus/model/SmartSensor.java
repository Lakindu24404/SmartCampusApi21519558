// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
    // main class here
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
    // return id
    public String getId() {
        return id;
    }
    // set id value
    public void setId(String id) {
        this.id = id;
    }
    // standard getter
    public String getType() {
        return type;
    }
    // setting data
    public void setType(String type) {
        this.type = type;
    }
    // standard getter
    public String getStatus() {
        return status;
    }
    // setting data
    public void setStatus(String status) {
        this.status = status;
    }
    // standard getter
    public double getCurrentValue() {
        return currentValue;
    }
    // setting data
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
    // standard getter
    public String getRoomId() {
        return roomId;
    }
    // setting data
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
