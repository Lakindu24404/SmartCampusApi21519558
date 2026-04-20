// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.service;

import com.smartcampus.model.Sensor;
import com.smartcampus.repository.SensorRepository;
import com.smartcampus.repository.RoomRepository;
import com.smartcampus.model.Room;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class SensorService { // handles logic for iot sensors

    private final SensorRepository sensorRepository = new SensorRepository();
    private final RoomRepository roomRepository = new RoomRepository();

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor getSensorById(String id) {
        return sensorRepository.findById(id);
    }

    public void createSensor(Sensor sensor) {
        // room must exist to add sensor
        Room room = roomRepository.findById(sensor.getRoomId());
        if (room == null) {
            throw new NotFoundException("no room found");
        }
        
        sensorRepository.save(sensor);
        
        // link sensor to room
        room.addSensorId(sensor.getId());
        roomRepository.save(room);
    }

    public void deleteSensor(String id) {
        Sensor sensor = sensorRepository.findById(id);
        if (sensor == null) return;
        
        // remove link from room
        Room room = roomRepository.findById(sensor.getRoomId());
        if (room != null) {
            room.removeSensorId(id);
            roomRepository.save(room);
        }
        
        sensorRepository.delete(sensor);
    }

    public void updateSensor(Sensor sensor) {
        sensorRepository.save(sensor);
    }
}
