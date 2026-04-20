package com.smartcampus.repository;

import com.smartcampus.model.SensorReading;
import com.smartcampus.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class SensorReadingRepository extends BaseRepository<SensorReading, String> {
    public SensorReadingRepository() {
        super(SensorReading.class);
    }

    public List<SensorReading> findBySensorId(String sensorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from SensorReading where sensorId = :sid", SensorReading.class)
                    .setParameter("sid", sensorId)
                    .list();
        }
    }
}
