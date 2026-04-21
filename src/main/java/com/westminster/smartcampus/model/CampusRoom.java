// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.model;
import java.util.ArrayList;
import java.util.List;
    // main class here
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
    // return id
    public String getId() {
        return id;
    }
    // set id value
    public void setId(String id) {
        this.id = id;
    }
    // standard getter
    public String getName() {
        return name;
    }
    // setting data
    public void setName(String name) {
        this.name = name;
    }
    // standard getter
    public int getCapacity() {
        return capacity;
    }
    // setting data
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    // standard getter
    public List<String> getSensorIds() {
        return sensorIds;
    }
    // setting data
    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = (sensorIds == null) ? new ArrayList<>() : sensorIds;
    }
}
