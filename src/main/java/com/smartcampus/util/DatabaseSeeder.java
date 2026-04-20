package com.smartcampus.util;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.AuthService;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Automatically seeds the database with initial data on application startup.
 */
@WebListener
public class DatabaseSeeder implements ServletContextListener {

    private final RoomService roomService = new RoomService();
    private final SensorService sensorService = new SensorService();
    private final AuthService authService = new AuthService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1. Seed Users
            if (new com.smartcampus.repository.UserRepository().findByUsername("admin") == null) {
                authService.register("admin", "password123", "ADMIN");
                authService.register("student", "password123", "USER");
            }

            // 2. Seed Rooms (if empty)
            if (roomService.getAllRooms().isEmpty()) {
                Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
                Room r2 = new Room("LAB-101", "Computer Science Lab A", 30);
                roomService.createRoom(r1);
                roomService.createRoom(r2);

                // 3. Seed Sensors
                sensorService.createSensor(new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301"));
                sensorService.createSensor(new Sensor("CO2-001", "CO2", "ACTIVE", 410.0, "LAB-101"));
            }
        } catch (Exception e) {
            System.err.println("Failed to seed database: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
    }
}
