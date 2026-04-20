package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.repository.RoomRepository;
import com.smartcampus.exception.RoomNotEmptyException;

import java.util.List;

public class RoomService {

    private final RoomRepository roomRepository = new RoomRepository();

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public void createRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(String roomId) {
        Room room = roomRepository.findById(roomId);
        if (room == null) return;

        // Business Logic: A room with active sensors cannot be deleted.
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        roomRepository.delete(room);
    }
}
