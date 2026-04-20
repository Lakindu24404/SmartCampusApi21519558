package com.smartcampus.service;

import com.smartcampus.model.Sensor;
import com.smartcampus.repository.SensorRepository;
import com.smartcampus.repository.RoomRepository;
import com.smartcampus.model.Room;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class SensorService {

    private final SensorRepository sensorRepository = new SensorRepository();
    private final RoomRepository roomRepository = new RoomRepository();

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor getSensorById(String id) {
        return sensorRepository.findById(id);
    }

    public void createSensor(Sensor sensor) {
        // Validate room exists
        Room room = roomRepository.findById(sensor.getRoomId());
        if (room == null) {
            throw new NotFoundException("Room with ID " + sensor.getRoomId() + " not found");
        }
        
        sensorRepository.save(sensor);
        
        // Update room's sensor list
        room.addSensorId(sensor.getId());
        roomRepository.save(room);
    }

    public void deleteSensor(String id) {
        Sensor sensor = sensorRepository.findById(id);
        if (sensor == null) return;
        
        // Remove from room
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
