// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.data;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataStore { // single in-memory store for all campus data

    public static final Map<String, Room>          ROOMS    = new LinkedHashMap<>();
    public static final Map<String, Sensor>        SENSORS  = new LinkedHashMap<>();
    public static final Map<String, SensorReading> READINGS = new LinkedHashMap<>();

    static {
        // --- seed rooms ---
        Room r1 = new Room("LIB-301", "Library Area 1", 50);
        Room r2 = new Room("LAB-101", "Computer Lab 1", 30);
        r1.addSensorId("TEMP-001");
        r2.addSensorId("CO2-001");
        ROOMS.put(r1.getId(), r1);
        ROOMS.put(r2.getId(), r2);

        // --- seed sensors ---
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5,  "LIB-301");
        Sensor s2 = new Sensor("CO2-001",  "CO2",         "ACTIVE", 410.0, "LAB-101");
        SENSORS.put(s1.getId(), s1);
        SENSORS.put(s2.getId(), s2);
    }
}
