// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
import java.util.ArrayList;
import java.util.List;
public class CampusRoom {
    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds = new ArrayList<>();
    public CampusRoom() {
    }
    public CampusRoom(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    // no need for complicated stuff here
    public String getName() {
        return name;
    }
    // no need for complicated stuff here
    public void setName(String name) {
        this.name = name;
    }
    // keeping it simple
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public List<String> getSensorIds() {
        return sensorIds;
    }
    // doing it my way here
    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = (sensorIds == null) ? new ArrayList<>() : sensorIds;
    }
}
