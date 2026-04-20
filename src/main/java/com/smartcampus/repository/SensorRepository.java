// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.model.Sensor;

public class SensorRepository extends BaseRepository<Sensor, String> { // db access for sensors
    public SensorRepository() {
        super(Sensor.class);
    }
}
