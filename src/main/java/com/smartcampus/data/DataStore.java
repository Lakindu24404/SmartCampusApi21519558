// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.data;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;
import java.util.List;

public class DataStore { // bridge to the new service layer for backward compatibility
    
    private static final RoomService roomService = new RoomService();
    private static final SensorService sensorService = new SensorService();

    public static List<Room> getRooms() { return roomService.getAllRooms(); }
    public static List<Sensor> getSensors() { return sensorService.getAllSensors(); }
}
