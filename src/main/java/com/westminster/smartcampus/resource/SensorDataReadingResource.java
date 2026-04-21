// Author: 2151955/ 20241937 / Lakindu Jayathilaka

package com.westminster.smartcampus.resource;
import com.westminster.smartcampus.exception.BadRequestException;
import com.westminster.smartcampus.exception.NotFoundException;
import com.westminster.smartcampus.exception.SensorUnavailableException;
import com.westminster.smartcampus.model.SmartSensor;
import com.westminster.smartcampus.model.SensorDataReading;
import com.westminster.smartcampus.store.MemoryDataStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
@Path("/readings")
@Produces(MediaType.APPLICATION_JSON)
public class SensorDataReadingResource {
    private final String sensorId;
    private final MemoryDataStore dataStore = MemoryDataStore.getInstance();
    public SensorDataReadingResource(@PathParam("sensorId") String sensorId) {
        this.sensorId = sensorId;
    }
    public String getSensorId() {
        return sensorId;
    }
    @GET
    // just what i need
    public Response getReadingHistory() {
        SmartSensor smartSensor = dataStore.getSensor(sensorId);
        if (smartSensor == null) {
            throw new NotFoundException("SmartSensor not found");
        }
        List<SensorDataReading> history = dataStore.getReadingsForSensor(sensorId);
        return Response.ok(history).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    // setting this up
    public Response addReading(SensorDataReading reading) {
        SmartSensor smartSensor = dataStore.getSensor(sensorId);
        if (smartSensor == null) {
            throw new NotFoundException("SmartSensor not found");
        }
        if (smartSensor.getStatus() != null && smartSensor.getStatus().equalsIgnoreCase("OFFLINE")) {
            throw new SensorUnavailableException("SmartSensor is OFFLINE and cannot accept readings");
        }
        if (reading == null || reading.getId() == null || reading.getId().trim().isEmpty()) {
            throw new BadRequestException("Reading id is required");
        }
        dataStore.addReading(sensorId, reading);
        smartSensor.setCurrentValue(reading.getValue());
        dataStore.upsertSensor(smartSensor);
        return Response.created(URI.create("/api/v1/sensors/" + sensorId + "/readings/" + reading.getId()))
                .entity(reading)
                .build();
    }
}
