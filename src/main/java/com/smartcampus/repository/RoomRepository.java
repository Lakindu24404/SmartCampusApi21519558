// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;

import java.util.Map;

public class RoomRepository extends BaseRepository<Room, String> { // in-memory store for rooms

    @Override
    protected Map<String, Room> store() {
        return DataStore.ROOMS;
    }

    @Override
    protected String getId(Room entity) {
        return entity.getId();
    }
}
