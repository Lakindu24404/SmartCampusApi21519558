// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Room Management", description = "Operations for managing physical campus locations")
public class RoomResource { // api endpoints for rooms

    private final RoomService roomService = new RoomService();

    @GET
    @Operation(summary = "List all rooms", description = "Returns a complete list of all campus rooms.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved room list")
    public Response getAllRooms() {
        return Response.ok(roomService.getAllRooms()).build();
    }

    @POST
    @Operation(summary = "Create a new room", description = "Adds a new room to the system.")
    @ApiResponse(responseCode = "201", description = "Room successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public Response createRoom(@Valid Room room) {
        roomService.createRoom(room);
        return Response.created(URI.create("/api/v1/rooms/" + room.getId()))
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    @Operation(summary = "Get room by ID", description = "Fetches detailed metadata for a specific room.")
    @ApiResponse(responseCode = "200", description = "Room found")
    @ApiResponse(responseCode = "404", description = "Room not found")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            throw new NotFoundException("no room with id " + roomId);
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Operation(summary = "Delete a room", description = "Removes a room from the system. Blocked if sensors are assigned.")
    @ApiResponse(responseCode = "204", description = "Room successfully deleted")
    @ApiResponse(responseCode = "409", description = "Conflict - Room has active sensors")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        roomService.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
