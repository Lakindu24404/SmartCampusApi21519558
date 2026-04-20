// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.util;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseSeeder implements ServletContextListener { // seed basic data on startup

    private final RoomService roomService = new RoomService();
    private final SensorService sensorService = new SensorService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {


            // seed some rooms
            if (roomService.getAllRooms().isEmpty()) {
                Room r1 = new Room("LIB-301", "Library Area 1", 50);
                Room r2 = new Room("LAB-101", "Computer Lab 1", 30);
                roomService.createRoom(r1);
                roomService.createRoom(r2);

                // add initial sensors
                sensorService.createSensor(new Sensor("TEMP-001", "Temp", "ACTIVE", 22.5, "LIB-301"));
                sensorService.createSensor(new Sensor("CO2-001", "CO2", "ACTIVE", 410.0, "LAB-101"));
            }
        } catch (Exception e) {
            System.err.println("DB seed failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
    }
}
