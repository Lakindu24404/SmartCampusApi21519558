// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.resource;
import com.westminster.smartcampus.exception.BadRequestException;
import com.westminster.smartcampus.exception.ConflictException;
import com.westminster.smartcampus.exception.NotFoundException;
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
    // just what i need
    public Response getRoomById(@PathParam("id") String id) {
        CampusRoom campusRoom = dataStore.getRoom(id);
        if (campusRoom == null) {
            throw new NotFoundException("CampusRoom not found");
        }
        return Response.ok(campusRoom).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(CampusRoom campusRoom) {
        if (campusRoom == null || campusRoom.getId() == null || campusRoom.getId().trim().isEmpty()) {
            throw new BadRequestException("CampusRoom id is required");
        }
        if (campusRoom.getName() == null || campusRoom.getName().trim().isEmpty()) {
            throw new BadRequestException("CampusRoom name is required");
        }
        if (campusRoom.getCapacity() < 0) {
            throw new BadRequestException("CampusRoom capacity must be >= 0");
        }
        if (campusRoom.getSensorIds() == null) {
            campusRoom.setSensorIds(null);
        }
        boolean alreadyExists = dataStore.getRoom(campusRoom.getId()) != null;
        if (alreadyExists) {
            throw new ConflictException("CampusRoom with id already exists");
        }
        dataStore.upsertRoom(campusRoom);
        return Response.created(URI.create("/api/v1/rooms/" + campusRoom.getId()))
                .entity(campusRoom)
                .build();
    }
    @DELETE
    @Path("/{id}")
    // keeping it simple
    public Response deleteRoom(@PathParam("id") String id) {
        CampusRoom campusRoom = dataStore.getRoom(id);
        if (campusRoom == null) {
            throw new NotFoundException("CampusRoom not found");
        }
        if (campusRoom.getSensorIds() != null && !campusRoom.getSensorIds().isEmpty()) {
            throw new ConflictException("CampusRoom cannot be deleted while sensors are assigned");
        }
        dataStore.deleteRoom(id);
        return Response.noContent().build();
    }
}
