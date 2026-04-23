/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api.resource;

import com.smartcampus.api.exception.SensorUnavailableException;
import com.smartcampus.api.model.ErrorResponse;
import com.smartcampus.api.DataStore;
import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author User
 */
@Path("/sensors/{id}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // GET all readings for a sensor
    @GET
    public Response getReadings(@PathParam("id") String sensorId) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404,"Sensor not found"))
                    .build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
        }

        return Response.ok(sensorReadings).build();
    }

    // POST add a new reading to a sensor
    @POST
    public Response addReading(@PathParam("id") String sensorId, SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(404, "Sensor not found"))
                .build();
        }

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(400, "Reading ID is required"))
                .build();
        }

        if (reading.getTimestamp() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(400, "Reading timestamp must be greater than 0"))
                .build();
        }
    
        // Sensor must be ACTIVE
        if (!sensor.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new SensorUnavailableException("Sensor is not available to accept readings");
        }

        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            DataStore.readings.put(sensorId, sensorReadings);
        }
    
        // Prevent duplicate reading IDs for the same sensor
        for (SensorReading existingReading : sensorReadings) {
            if (existingReading.getId().equalsIgnoreCase(reading.getId())) {
                return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, "A reading with this ID already exists for this sensor"))
                    .build();
            }
        }

        sensorReadings.add(reading);
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
            .entity(reading)
            .build();
    }
}

