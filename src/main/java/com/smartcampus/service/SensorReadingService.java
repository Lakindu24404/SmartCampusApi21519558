// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.service;

import com.smartcampus.model.SensorReading;
import com.smartcampus.model.Sensor;
import com.smartcampus.repository.SensorReadingRepository;
import com.smartcampus.repository.SensorRepository;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

public class SensorReadingService { // handles logic for recording history

    private final SensorReadingRepository readingRepository = new SensorReadingRepository();
    private final SensorRepository sensorRepository = new SensorRepository();

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        // sensor must exist
        if (sensorRepository.findById(sensorId) == null) {
            throw new NotFoundException("sensor missing");
        }
        return readingRepository.findBySensorId(sensorId);
    }

    public void addReading(String sensorId, double value) {
        Sensor sensor = sensorRepository.findById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("sensor missing");
        }
        
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new com.smartcampus.exception.SensorUnavailableException("Sensor is in MAINTENANCE mode and cannot accept readings.");
        }

        // update the sensor's current value
        sensor.setCurrentValue(value);
        sensorRepository.save(sensor);

        // log the historical reading
        SensorReading reading = new SensorReading(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                value,
                sensorId
        );
        readingRepository.save(reading);
    }
}
