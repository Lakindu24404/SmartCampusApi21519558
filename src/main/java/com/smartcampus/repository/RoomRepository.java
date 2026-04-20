package com.smartcampus.repository;

import com.smartcampus.model.Room;

public class RoomRepository extends BaseRepository<Room, String> {
    public RoomRepository() {
        super(Room.class);
    }
}
