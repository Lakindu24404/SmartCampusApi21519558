// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.model.Room;

public class RoomRepository extends BaseRepository<Room, String> { // db access for rooms
    public RoomRepository() {
        super(Room.class);
    }
}
