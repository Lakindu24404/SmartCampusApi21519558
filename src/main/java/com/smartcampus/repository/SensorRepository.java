// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Sensor;

import java.util.Map;

public class SensorRepository extends BaseRepository<Sensor, String> { // in-memory store for sensors

    @Override
    protected Map<String, Sensor> store() {
        return DataStore.SENSORS;
    }

    @Override
    protected String getId(Sensor entity) {
        return entity.getId();
    }
}
