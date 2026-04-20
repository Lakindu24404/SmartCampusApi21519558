package com.smartcampus.repository;

import com.smartcampus.model.Sensor;

public class SensorRepository extends BaseRepository<Sensor, String> {
    public SensorRepository() {
        super(Sensor.class);
    }
}
