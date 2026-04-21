// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.store;
import com.westminster.smartcampus.model.CampusRoom;
import com.westminster.smartcampus.model.SmartSensor;
import com.westminster.smartcampus.model.SensorDataReading;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public final class MemoryDataStore {
    private static final MemoryDataStore INSTANCE = new MemoryDataStore();
    private final Map<String, CampusRoom> rooms = new ConcurrentHashMap<>();
    private final Map<String, SmartSensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, List<SensorDataReading>> readingsBySensorId = new ConcurrentHashMap<>();
    private MemoryDataStore() {
    }
    public static MemoryDataStore getInstance() {
        return INSTANCE;
    }
    public Collection<CampusRoom> getAllRooms() {
        return rooms.values();
    }
    public CampusRoom getRoom(String id) {
        return rooms.get(id);
    }
    public CampusRoom upsertRoom(CampusRoom campusRoom) {
        if (campusRoom == null || campusRoom.getId() == null) {
            throw new IllegalArgumentException("CampusRoom and campusRoom.id must not be null");
        }
        return rooms.put(campusRoom.getId(), campusRoom);
    }
    public CampusRoom deleteRoom(String id) {
        return rooms.remove(id);
    }
    public Collection<SmartSensor> getAllSensors() {
        return sensors.values();
    }
    public SmartSensor getSensor(String id) {
        return sensors.get(id);
    }
    public SmartSensor upsertSensor(SmartSensor smartSensor) {
        if (smartSensor == null || smartSensor.getId() == null) {
            throw new IllegalArgumentException("SmartSensor and smartSensor.id must not be null");
        }
        return sensors.put(smartSensor.getId(), smartSensor);
    }
    public SmartSensor deleteSensor(String id) {
        readingsBySensorId.remove(id);
        return sensors.remove(id);
    }
    // this works better
    public List<SensorDataReading> getReadingsForSensor(String sensorId) {
        if (sensorId == null) {
            throw new IllegalArgumentException("sensorId must not be null");
        }
        List<SensorDataReading> list = readingsBySensorId.get(sensorId);
        if (list == null) {
            return Collections.emptyList();
        }
        synchronized (list) {
            return new ArrayList<>(list);
        }
    }
    public void addReading(String sensorId, SensorDataReading reading) {
        if (sensorId == null) {
            throw new IllegalArgumentException("sensorId must not be null");
        }
        if (reading == null) {
            throw new IllegalArgumentException("reading must not be null");
        }
        List<SensorDataReading> list = readingsBySensorId.computeIfAbsent(
                sensorId,
                k -> Collections.synchronizedList(new ArrayList<>())
        );
        list.add(reading);
    }
}
