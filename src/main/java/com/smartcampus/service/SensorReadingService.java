package com.smartcampus.service;

import com.smartcampus.model.SensorReading;
import com.smartcampus.model.Sensor;
import com.smartcampus.repository.SensorReadingRepository;
import com.smartcampus.repository.SensorRepository;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

public class SensorReadingService {

    private final SensorReadingRepository readingRepository = new SensorReadingRepository();
    private final SensorRepository sensorRepository = new SensorRepository();

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        // Validate sensor exists
        if (sensorRepository.findById(sensorId) == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found");
        }
        return readingRepository.findBySensorId(sensorId);
    }

    public void addReading(String sensorId, double value) {
        Sensor sensor = sensorRepository.findById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found");
        }

        // Update sensor's current value
        sensor.setCurrentValue(value);
        sensorRepository.save(sensor);

        // Record historical reading
        SensorReading reading = new SensorReading(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                value,
                sensorId
        );
        readingRepository.save(reading);
    }
}
