package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;
import com.smartcampus.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final SensorService sensorService = new SensorService();

    @GET
    public Response getAllSensors() {
        return Response.ok(sensorService.getAllSensors()).build();
    }

    @POST
    @Secured({"ADMIN"})
    public Response createSensor(@Valid Sensor sensor) {
        sensorService.createSensor(sensor);
        return Response.created(URI.create("/api/v1/sensors/" + sensor.getId()))
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorService.getSensorById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found");
        }
        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{sensorId}")
    @Secured({"ADMIN"})
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        sensorService.deleteSensor(sensorId);
        return Response.noContent().build();
    }
}
