package com.smartcampus.resource;

import com.smartcampus.data.DataStore;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

/**
 * Room Resource — Part 2
 *
 * Manages the /api/v1/rooms collection.
 *
 * Endpoints:
 *   GET    /api/v1/rooms           → list all rooms
 *   POST   /api/v1/rooms           → create a new room
 *   GET    /api/v1/rooms/{roomId}  → get a single room
 *   DELETE /api/v1/rooms/{roomId}  → delete a room (blocked if sensors assigned)
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // ── GET /api/v1/rooms ─────────────────────────────────────────────────────────
    /**
     * Returns a list of all rooms currently stored in the system.
     * Full room objects are returned so clients don't need a second round-trip.
     */
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    // ── POST /api/v1/rooms ────────────────────────────────────────────────────────
    /**
     * Creates a new room.
     * If no ID is provided in the body, a UUID is auto-generated.
     * Returns 201 Created with a Location header pointing to the new resource.
     */
    @POST
    public Response createRoom(Room room) {
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorBody("BAD_REQUEST", "Request body must be a valid Room JSON object."))
                    .build();
        }

        // Generate ID if not provided
        if (room.getId() == null || room.getId().isBlank()) {
            room.setId("ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Reject duplicate IDs
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorBody("DUPLICATE_ID", "A room with ID '" + room.getId() + "' already exists."))
                    .build();
        }

        DataStore.rooms.put(room.getId(), room);

        URI location = URI.create("/api/v1/rooms/" + room.getId());
        return Response.created(location).entity(room).build();
    }

    // ── GET /api/v1/rooms/{roomId} ────────────────────────────────────────────────
    /**
     * Fetches detailed metadata for a specific room by its ID.
     * Returns 404 if the room does not exist.
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room with ID '" + roomId + "' was not found.");
        }
        return Response.ok(room).build();
    }

    // ── DELETE /api/v1/rooms/{roomId} ─────────────────────────────────────────────
    /**
     * Decommissions a room.
     *
     * Business Logic Constraint: A room with active sensors cannot be deleted.
     * This prevents data orphans (sensors referencing a non-existent room).
     *
     * Idempotency: The first DELETE returns 204. A second DELETE on the same
     * room returns 404, because the resource no longer exists. This is technically
     * NOT fully idempotent but is the correct and most common REST practice.
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            throw new NotFoundException("Room with ID '" + roomId + "' was not found.");
        }

        // Safety check: cannot delete a room with assigned sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();  // 204
    }

    // ── Helper ────────────────────────────────────────────────────────────────────
    private Map<String, String> errorBody(String error, String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", error);
        body.put("message", message);
        return body;
    }
}
