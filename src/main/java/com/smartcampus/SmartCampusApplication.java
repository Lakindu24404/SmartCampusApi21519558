// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus;

import com.smartcampus.filter.ApiLoggingFilter;
import com.smartcampus.resource.*;
import com.smartcampus.exception.*;
import com.smartcampus.exception.mapper.*;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
@OpenAPIDefinition(
    info = @Info(
        title = "Smart Campus IoT API",
        version = "1.0.0",
        description = "RESTful API for managing campus rooms and IoT sensors using in-memory storage.",
        license = @License(name = "University of Westminster")
    ),
    servers = @Server(url = "/api/v1")
)
public class SmartCampusApplication extends Application { // app entry point

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // register resources
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorReadingResource.class);

        // register exception mappers
        classes.add(ValidationExceptionMapper.class);
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);

        // register filters
        classes.add(ApiLoggingFilter.class);

        // swagger resources
        classes.add(OpenApiResource.class);
        classes.add(AcceptHeaderOpenApiResource.class);

        return classes;
    }
}
