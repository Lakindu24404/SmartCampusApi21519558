# Smart Campus API — Overview and Implementation Guide - 21519558

---

## 🎓 Academic Information

- **Name:** Lakindu Jayathilaka  
- **University Registration No:** 21519558 / 20241937  
- **Degree:** (Hons) Computer Science  
- **Module:** Client Server Architecture  

---

## 1. Introduction

The Smart Campus API is designed to facilitate the management of campus infrastructure through a RESTful web service architecture. It enables the handling of key entities such as rooms, sensors, and sensor readings. The API follows standard HTTP conventions and is implemented using JAX-RS, ensuring scalability, modularity, and ease of integration with client applications.

## 2. API Architecture and Design

The API is structured around a base path defined as `/api/v1`, configured using the `@ApplicationPath` annotation within the JAX-RS application. This versioned approach supports future extensibility and backward compatibility.

### 2.1 Resource Structure

The system is composed of three primary resource categories:

- **Rooms (`/rooms`)**
  This resource provides Create, Read, Update, and Delete (CRUD) operations for Room entities. Each room is characterised by a unique identifier (`id`), a descriptive name (`name`), and a seating capacity (`capacity`).
- **Sensors (`/sensors`)**
  This resource manages Sensor entities, supporting full CRUD functionality. Each sensor includes attributes such as `id`, `type`, `status`, `currentValue`, and an associated `roomId`. It is important to note that the sensor identifier is immutable after creation to maintain data integrity.
- **Sensor Readings (`/sensors/{sensorId}/readings`)**
  This is implemented as a sub-resource under the Sensor entity. It handles SensorReading objects, which include `id`, `timestamp`, and `value`. When a new reading is recorded, the corresponding sensor’s `currentValue` is automatically updated to reflect the latest measurement.

### 2.2 Data Storage

The system utilises an in-memory DataStore for persistence. While this approach simplifies development and testing, it implies that all stored data will be lost upon server restart. Therefore, it is primarily suitable for prototyping or non-production environments.

### 2.3 Error Handling Strategy

The API incorporates a structured error-handling mechanism using standard HTTP status codes to communicate the outcome of client requests:

- `400 Bad Request` – Indicates malformed or invalid input data.
- `404 Not Found` – Returned when a requested resource does not exist.
- `409 Conflict` – Signals a violation of resource uniqueness or state conflicts.
- `422 Unprocessable Entity` – Represents semantically incorrect data despite valid syntax.
- `403 Forbidden` – Used when a request violates defined business rules or constraints. 

## 3. Deployment and Execution

### 3.1 Prerequisites

To successfully build and execute the application, the following requirements must be met:

- Java Development Kit (JDK) version 11 or higher
- Apache Maven build automation tool

The application is accessible by default at:
`http://localhost:8080`

### 3.2 Execution Methods

#### A. Embedded Jetty Server
1. Open a PowerShell terminal within the project directory.
2. Execute the build command:
   ```bash
   mvn clean package
   ```
3. Start the embedded Jetty server:
   ```bash
   mvn jetty:run
   ```
4. Access the API via:
   `http://localhost:8080/api/v1`

#### B. Deployment on Apache Tomcat
1. Build the project using:
   ```bash
   mvn clean package
   ```
2. Copy the generated `ROOT.war` file from the `target` directory into the Tomcat `webapps` directory (`%TOMCAT_HOME%\webapps`).
3. Restart or start the Tomcat server.
4. Access the deployed application at:
   `http://localhost:8080/api/v1`

#### C. NetBeans Integrated Deployment
1. Import the Maven project into the NetBeans IDE.
2. Ensure the project’s `finalName` is configured as `ROOT`, or set the context path to `/` via Project Properties.
3. Perform a clean build and deploy the project.
4. Start the application server and access the API at:
   `http://localhost:8080/api/v1`

## 4. API Usage Examples

The following examples demonstrate typical interactions with the API using `curl` commands:

### Creating a Room (HTTP 201 Created)

```bash
curl -i -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library Quiet Study","capacity":40}'
```

### Registering a Sensor (HTTP 201 Created)

```bash
curl -i -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0,"roomId":"LIB-301"}'
```

### Retrieving Sensor List (HTTP 200 OK)

```bash
curl -i http://localhost:8080/api/v1/sensors
```

### Submitting a Sensor Reading (HTTP 201 Created)

```bash
curl -i -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"R-001","timestamp":1710000000000,"value":23.5}'
```

### Deleting a Sensor (HTTP 204 No Content)

```bash
curl -i -X DELETE http://localhost:8080/api/v1/sensors/TEMP-001
```

---

## 4. Report — Answers to Coursework Questions

### 🔹 Part 1: Fundamentals

#### Question 1.1: Lifecycle of a JAX-RS Resource

Resource classes in JAX-RS follow a request-based lifecycle, where a new resource class instance is created for each HTTP request that arrives at the web service. Once the request processing ends and the response is sent back to the client, the resource class instance is destroyed.

While this approach ensures thread safety on an object level, it restricts certain operations related to storing in-memory data. This is because different requests are served using different instances of the class, hence, any data stored in class member variables will not be preserved in the next request.

The current implementation utilizes the singleton pattern to implement its data store in memory. This enables the preservation and reuse of information across different requests.

Another consideration made in implementing the application is its ability to handle requests from multiple clients simultaneously. This requires proper handling of concurrency issues that might arise when several clients access shared resources simultaneously. This has been handled using thread-safe collections like `ConcurrentHashMap`.

#### Q2: Hypermedia (HATEOAS) as an Advanced RESTful Design Principle

Hypermedia as the Engine of Application State (HATEOAS) represents an advanced constraint of RESTful architecture. It requires that API responses include hypermedia links, enabling clients to dynamically navigate available resources and actions.

**Advantages over Static API Design**

The adoption of HATEOAS provides several benefits:

- **Decoupling of client and server:** Clients do not need to hardcode endpoint URLs, as navigation is guided through links provided in responses.
- **Improved evolvability:** APIs can evolve without breaking existing clients, as clients rely on discoverable links rather than fixed URI structures.
- **Enhanced maintainability:** Reduces dependency on external documentation by embedding navigation logic within responses.

In the Smart Campus API, the inclusion of hypermedia links through the `ApiDiscoveryResource` supports a more flexible and scalable architecture.

---

### 🔹 Part 2: Resource Representation and HTTP Semantics

#### Q3: Returning Identifiers vs Full Resource Representations

The design of the `GET /rooms` endpoint involves a trade-off between returning minimal data (only identifiers) and full resource representations.

Returning only identifiers reduces response payload size, resulting in lower bandwidth consumption and improved performance for clients that require only a list of selectable items.

Conversely, returning full `CampusRoom` objects provides complete information in a single request, reducing the need for additional API calls. However, this increases payload size and processing overhead.

A balanced approach involves returning summary representations (e.g., ID and name) or implementing pagination mechanisms. In the current implementation, returning full objects is acceptable for simplicity, though scalability concerns must be considered as data volume increases.

#### Q4: Idempotency of the DELETE Operation

An HTTP method is considered idempotent if multiple identical requests result in the same server state.

In the Smart Campus API:
- The initial `DELETE /rooms/{id}` request removes the specified resource.
- Subsequent identical requests return a **404 Not Found** response, as the resource no longer exists.

Despite the change in response code, the server state remains unchanged after the first request, thereby satisfying the definition of idempotency.

---

### 🔹 Part 3: Content Negotiation and Request Design

#### Q5: Implications of `@Consumes(MediaType.APPLICATION_JSON)`

The use of `@Consumes(MediaType.APPLICATION_JSON)` in endpoints such as those in `SmartSensorResource` enforces that only requests with a Content-Type of application/json are accepted.

If a client submits data using an unsupported media type (e.g., text/plain or application/xml), the JAX-RS runtime will reject the request and typically return a **415 Unsupported Media Type** response.

This mechanism ensures strict adherence to API contracts and prevents ambiguity in request processing.

#### Q6: Advantages of Query Parameters for Filtering

The use of query parameters, such as `GET /sensors?type=CO2`, is preferred for implementing filtering functionality.

This approach offers:
- **Flexibility:** Multiple filters can be combined (e.g., `?type=CO2&status=ACTIVE`).
- **Clarity:** Maintains a consistent representation of the resource collection (`/sensors`).
- **Scalability:** Simplifies the addition of new filtering, sorting, and pagination features.

In contrast, embedding filters within the URI path (e.g., `/sensors/type/CO2`) reduces flexibility and complicates routing design.

---

### 🔹 Part 4: Resource Structuring

#### Q7: Benefits of the Sub-Resource Locator Pattern

The Sub-Resource Locator pattern is employed to manage hierarchical resources, such as sensor data readings associated with individual sensors.

For instance, `SmartSensorResource` delegates requests to `SensorDataReadingResource` for handling `/sensors/{id}/readings`.

Key benefits include:
- **Separation of concerns:** Each resource class has a clearly defined responsibility.
- **Improved maintainability:** Prevents excessive growth of a single resource class.
- **Enhanced testability:** Individual components can be independently tested.
- **Logical URI mapping:** Code structure aligns with URI hierarchy, improving readability.

---

### 🔹 Part 5: Error Handling and Observability

#### Q8: Use of HTTP 422 (Unprocessable Entity)

The HTTP status code **422 Unprocessable Entity** is used when a request is syntactically valid but semantically incorrect.

For example, when a client attempts to create a `SmartSensor` with a roomId that does not exist:
- The endpoint (`POST /sensors`) is valid.
- The request body is correctly formatted JSON.
- However, the referenced entity is invalid.

In this scenario, returning 422 provides more precise feedback than 404, which is reserved for non-existent endpoints.

#### Q9: Risks of Exposing Stack Traces in API Responses

Returning stack traces in API responses poses significant security risks, including:
- Exposure of internal implementation details (e.g., class names and frameworks)
- Disclosure of file paths and configuration information
- Increased vulnerability to targeted attacks

To mitigate these risks, the Smart Campus API returns generic error responses for unexpected failures while providing meaningful but controlled messages for known errors.

#### Q10: Advantages of Using JAX-RS Filters for Logging

Logging is implemented using JAX-RS filters (`ContainerRequestFilter` and `ContainerResponseFilter`) rather than manual logging within resource methods.

This approach provides:
- **Consistency:** Uniform logging format across all endpoints
- **Comprehensive coverage:** Logs include both successful and failed requests
- **Reduced duplication:** Eliminates repetitive logging code
- **Centralised control:** Logging behaviour can be modified in a single location

By externalising logging as a cross-cutting concern, resource classes such as `CampusRoomResource` and `SmartSensorResource` remain focused on core application logic.

---
## ⚠️ Error Handling

Example error response:

```json
{
  "error": "Room not empty",
  "status": 409
}
```

---

## 🏁 Conclusion

This API demonstrates:

- RESTful design principles
- Proper HTTP usage
- Clean architecture
- Robust error handling
- In-memory data management as required

---

## 🎥 Video Demonstration

Link- https://youtu.be/CEkO6fk4L9c
