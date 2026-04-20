// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.repository.RoomRepository;
import com.smartcampus.exception.mapper.RoomNotEmptyExceptionMapper; // Note: using mapper class name if needed or exception
import com.smartcampus.exception.RoomNotEmptyException; // assuming this exists or I'll check

import java.util.List;

public class RoomService { // handles logic for campus rooms

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

        // can't delete rooms with sensors in them
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        roomRepository.delete(room);
    }
}
