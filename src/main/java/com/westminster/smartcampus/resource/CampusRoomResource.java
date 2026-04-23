// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.resource;
import com.westminster.smartcampus.exception.BadRequestException;
import com.westminster.smartcampus.exception.ConflictException;
import com.westminster.smartcampus.exception.NotFoundException;
import com.westminster.smartcampus.exception.RoomNotEmptyException;
import com.westminster.smartcampus.model.CampusRoom;
import com.westminster.smartcampus.store.MemoryDataStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class CampusRoomResource {
    private final MemoryDataStore dataStore = MemoryDataStore.getInstance();

    @GET
    public Collection<CampusRoom> getAllRooms() {
        return dataStore.getAllRooms();
    }

    @GET
    @Path("/{id}")
    public Response getRoomById(@PathParam("id") String id) {
        CampusRoom campusRoom = dataStore.getRoom(id);
        if (campusRoom == null) {
            throw new NotFoundException("Room not found: " + id);
        }
        return Response.ok(campusRoom).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(CampusRoom campusRoom) {
        if (campusRoom == null || campusRoom.getId() == null || campusRoom.getId().trim().isEmpty()) {
            throw new BadRequestException("Room id is required");
        }
        if (campusRoom.getName() == null || campusRoom.getName().trim().isEmpty()) {
            throw new BadRequestException("Room name is required");
        }
        if (campusRoom.getCapacity() < 0) {
            throw new BadRequestException("Room capacity must be >= 0");
        }
        if (campusRoom.getSensorIds() == null) {
            campusRoom.setSensorIds(null);
        }
        if (dataStore.getRoom(campusRoom.getId()) != null) {
            throw new ConflictException("Room with id '" + campusRoom.getId() + "' already exists");
        }
        dataStore.upsertRoom(campusRoom);
        return Response.created(URI.create("/api/v1/rooms/" + campusRoom.getId()))
                .entity(campusRoom)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        CampusRoom campusRoom = dataStore.getRoom(id);
        if (campusRoom == null) {
            throw new NotFoundException("Room not found: " + id);
        }
        // Business Logic: cannot delete a room that still has sensors assigned
        if (campusRoom.getSensorIds() != null && !campusRoom.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room '" + id + "' cannot be deleted because it still has " +
                campusRoom.getSensorIds().size() + " active sensor(s) assigned to it. " +
                "Please remove all sensors before decommissioning this room.");
        }
        dataStore.deleteRoom(id);
        return Response.noContent().build();
    }
}
