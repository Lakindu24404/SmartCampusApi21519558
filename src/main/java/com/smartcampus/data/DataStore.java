package com.smartcampus.data;

import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;
import com.smartcampus.service.SensorReadingService;

/**
 * DataStore — Superseded by Service and Repository Layers.
 * 
 * This class is kept for structural compatibility as requested, 
 * but now delegates all logic to the database-backed service layer.
 */
public class DataStore {

    private static final RoomService roomService = new RoomService();
    private static final SensorService sensorService = new SensorService();
    private static final SensorReadingService readingService = new SensorReadingService();

    // Private constructor - this class is a static bridge
    private DataStore() {}

    /**
     * @deprecated Use RoomService instead.
     */
    @Deprecated
    public static RoomService getRoomService() {
        return roomService;
    }

    /**
     * @deprecated Use SensorService instead.
     */
    @Deprecated
    public static SensorService getSensorService() {
        return sensorService;
    }

    /**
     * @deprecated Use SensorReadingService instead.
     */
    @Deprecated
    public static SensorReadingService getReadingService() {
        return readingService;
    }
}
