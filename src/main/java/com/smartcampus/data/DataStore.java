package com.smartcampus.data;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton in-memory data store for the Smart Campus API.
 *
 * Uses ConcurrentHashMap to ensure thread-safety for concurrent API requests.
 * JAX-RS creates a new resource instance per request by default, so all
 * shared state must live here as static fields — not in the resource classes.
 *
 * Pre-populated with sample data to facilitate immediate Postman testing.
 */
public class DataStore {

    // ── Primary Collections ──────────────────────────────────────────────────────
    public static final Map<String, Room>                    rooms    = new ConcurrentHashMap<>();
    public static final Map<String, Sensor>                  sensors  = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>>     readings = new ConcurrentHashMap<>();

    // ── Seed Data ────────────────────────────────────────────────────────────────
    static {
        // Sample Rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Science Lab A", 30);
        Room r3 = new Room("HALL-01", "Main Lecture Hall", 200);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);
        rooms.put(r3.getId(), r3);

        // Sample Sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001",  "CO2",         "ACTIVE", 410.0, "LAB-101");
        Sensor s3 = new Sensor("OCC-001",  "Occupancy",   "MAINTENANCE", 0.0, "LAB-101");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);

        // Link sensors back to their rooms
        r1.addSensorId(s1.getId());
        r2.addSensorId(s2.getId());
        r2.addSensorId(s3.getId());

        // Initialise empty reading logs for existing sensors
        readings.put(s1.getId(), new ArrayList<>());
        readings.put(s2.getId(), new ArrayList<>());
        readings.put(s3.getId(), new ArrayList<>());
    }

    // Private constructor — this class is never instantiated
    private DataStore() {}
}
