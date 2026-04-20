package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.service.RoomService;
import com.smartcampus.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * Room Resource — Upgraded with Service Layer, Persistence, and Security.
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final RoomService roomService = new RoomService();

    @GET
    public Response getAllRooms() {
        return Response.ok(roomService.getAllRooms()).build();
    }

    @POST
    @Secured({"ADMIN"})
    public Response createRoom(@Valid Room room) {
        roomService.createRoom(room);
        return Response.created(URI.create("/api/v1/rooms/" + room.getId()))
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            throw new NotFoundException("Room with ID " + roomId + " not found");
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Secured({"ADMIN"})
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        roomService.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
