// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.resource;
import com.westminster.smartcampus.exception.BadRequestException;
import com.westminster.smartcampus.exception.ConflictException;
import com.westminster.smartcampus.exception.LinkedResourceNotFoundException;
import com.westminster.smartcampus.exception.NotFoundException;
import com.westminster.smartcampus.model.CampusRoom;
import com.westminster.smartcampus.model.SmartSensor;
import com.westminster.smartcampus.store.MemoryDataStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SmartSensorResource {
    private final MemoryDataStore dataStore = MemoryDataStore.getInstance();

    @GET
    public Collection<SmartSensor> getSensors(@QueryParam("type") String type) {
        Collection<SmartSensor> all = dataStore.getAllSensors();
        if (type == null || type.trim().isEmpty()) {
            return all;
        }
        String wanted = type.trim();
        List<SmartSensor> filtered = new ArrayList<>();
        for (SmartSensor s : all) {
            if (s != null && s.getType() != null && s.getType().equalsIgnoreCase(wanted)) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        SmartSensor smartSensor = dataStore.getSensor(sensorId);
        if (smartSensor == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        return Response.ok(smartSensor).build();
    }

    // Sub-resource locator — no HTTP method annotation, just @Path
    @Path("/{sensorId}/readings")
    public SensorDataReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorDataReadingResource(sensorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSensor(SmartSensor smartSensor) {
        validateSensorForCreate(smartSensor);

        // 422 if the referenced roomId does not exist
        CampusRoom campusRoom = dataStore.getRoom(smartSensor.getRoomId());
        if (campusRoom == null) {
            throw new LinkedResourceNotFoundException(
                "Referenced roomId '" + smartSensor.getRoomId() + "' does not exist. " +
                "Please create the room first before registering a sensor in it.");
        }

        if (dataStore.getSensor(smartSensor.getId()) != null) {
            throw new ConflictException("Sensor with id '" + smartSensor.getId() + "' already exists");
        }

        dataStore.upsertSensor(smartSensor);

        // Keep room's sensorIds list in sync
        if (!campusRoom.getSensorIds().contains(smartSensor.getId())) {
            campusRoom.getSensorIds().add(smartSensor.getId());
            dataStore.upsertRoom(campusRoom);
        }
        return Response.created(URI.create("/api/v1/sensors/" + smartSensor.getId()))
                .entity(smartSensor)
                .build();
    }

    @PUT
    @Path("/{sensorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSensor(@PathParam("sensorId") String sensorId, SmartSensor updated) {
        SmartSensor existing = dataStore.getSensor(sensorId);
        if (existing == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        if (updated == null) {
            throw new BadRequestException("Sensor body is required");
        }
        if (updated.getId() != null && !updated.getId().trim().isEmpty() && !sensorId.equals(updated.getId())) {
            throw new BadRequestException("Sensor id cannot be changed via PUT");
        }
        if (updated.getType() == null || updated.getType().trim().isEmpty()) {
            throw new BadRequestException("Sensor type is required");
        }
        if (updated.getStatus() == null || updated.getStatus().trim().isEmpty()) {
            throw new BadRequestException("Sensor status is required");
        }
        if (updated.getRoomId() == null || updated.getRoomId().trim().isEmpty()) {
            throw new BadRequestException("Sensor roomId is required");
        }
        CampusRoom newRoom = dataStore.getRoom(updated.getRoomId());
        if (newRoom == null) {
            throw new LinkedResourceNotFoundException(
                "Referenced roomId '" + updated.getRoomId() + "' does not exist.");
        }
        // Un-link from old room if room changed
        if (existing.getRoomId() != null && !existing.getRoomId().equals(updated.getRoomId())) {
            CampusRoom oldRoom = dataStore.getRoom(existing.getRoomId());
            if (oldRoom != null && oldRoom.getSensorIds() != null) {
                oldRoom.getSensorIds().remove(sensorId);
                dataStore.upsertRoom(oldRoom);
            }
        }
        if (newRoom.getSensorIds() != null && !newRoom.getSensorIds().contains(sensorId)) {
            newRoom.getSensorIds().add(sensorId);
            dataStore.upsertRoom(newRoom);
        }
        updated.setId(sensorId);
        dataStore.upsertSensor(updated);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        SmartSensor existing = dataStore.getSensor(sensorId);
        if (existing == null) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        // Un-link from room
        if (existing.getRoomId() != null) {
            CampusRoom campusRoom = dataStore.getRoom(existing.getRoomId());
            if (campusRoom != null && campusRoom.getSensorIds() != null) {
                campusRoom.getSensorIds().remove(sensorId);
                dataStore.upsertRoom(campusRoom);
            }
        }
        dataStore.deleteSensor(sensorId);
        return Response.noContent().build();
    }

    private void validateSensorForCreate(SmartSensor smartSensor) {
        if (smartSensor == null || smartSensor.getId() == null || smartSensor.getId().trim().isEmpty()) {
            throw new BadRequestException("Sensor id is required");
        }
        if (smartSensor.getType() == null || smartSensor.getType().trim().isEmpty()) {
            throw new BadRequestException("Sensor type is required");
        }
        if (smartSensor.getStatus() == null || smartSensor.getStatus().trim().isEmpty()) {
            throw new BadRequestException("Sensor status is required");
        }
        if (smartSensor.getRoomId() == null || smartSensor.getRoomId().trim().isEmpty()) {
            throw new BadRequestException("Sensor roomId is required");
        }
    }
}
