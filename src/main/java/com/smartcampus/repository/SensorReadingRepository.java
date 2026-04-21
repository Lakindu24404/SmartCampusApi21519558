// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SensorReadingRepository extends BaseRepository<SensorReading, String> { // in-memory store for reading history

    @Override
    protected Map<String, SensorReading> store() {
        return DataStore.READINGS;
    }

    @Override
    protected String getId(SensorReading entity) {
        return entity.getId();
    }

    public List<SensorReading> findBySensorId(String sensorId) {
        return store().values().stream()
                .filter(r -> sensorId.equals(r.getSensorId()))
                .collect(Collectors.toList());
    }
}
