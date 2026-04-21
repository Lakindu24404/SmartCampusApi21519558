# Smart Campus API — Overview and Implementation Guide

## 1. Introduction

The Smart Campus API is designed to facilitate the management of campus infrastructure through a RESTful web service architecture. It enables the handling of key entities such as CampusRooms, SmartSensors, and SensorDataReadings. The API follows standard HTTP conventions and is implemented using JAX-RS, ensuring scalability, modularity, and ease of integration with client applications.

## 2. API Architecture and Design

The API is structured around a base path defined as `/api/v1`, configured using the `@ApplicationPath` annotation within the JAX-RS application. This versioned approach supports future extensibility and backward compatibility.

### 2.1 Resource Structure

The system is composed of three primary resource categories:

- **Rooms (`/rooms`)**
  This resource provides Create, Read, Update, and Delete (CRUD) operations for `CampusRoom` entities. Each room is characterised by a unique identifier (`id`), a descriptive name (`name`), and a seating capacity (`capacity`).
- **Sensors (`/sensors`)**
  This resource manages `SmartSensor` entities, supporting full CRUD functionality. Each sensor includes attributes such as `id`, `type`, `status`, `currentValue`, and an associated `roomId`. It is important to note that the sensor identifier is immutable after creation to maintain data integrity.
- **Sensor Readings (`/sensors/{sensorId}/readings`)**
  This is implemented as a sub-resource under the Sensor entity. It handles `SensorDataReading` objects, which include `id`, `timestamp`, and `value`. When a new reading is recorded, the corresponding sensor’s `currentValue` is automatically updated to reflect the latest measurement.

### 2.2 Data Storage

The system utilises an in-memory `MemoryDataStore` for persistence. While this approach simplifies development and testing, it implies that all stored data will be lost upon server restart. Therefore, it is primarily suitable for prototyping or non-production environments.

### 2.3 Error Handling Strategy

The API incorporates a structured error-handling mechanism using standard HTTP status codes to communicate the outcome of client requests:

- **400 Bad Request** – Indicates malformed or invalid input data.
- **404 Not Found** – Returned when a requested resource does not exist.
- **409 Conflict** – Signals a violation of resource uniqueness or state conflicts.
- **422 Unprocessable Entity** – Represents semantically incorrect data despite valid syntax.
- **403 Forbidden** – Used when a request violates defined business rules or constraints.

## 3. Deployment and Execution

### 3.1 Prerequisites

To successfully build and execute the application, the following requirements must be met:

- Java Development Kit (JDK) version 11 or higher
- Apache Maven build automation tool

The application is accessible by default at:
`http://localhost:8080/api/v1`

### 3.2 Execution Methods

**A. Embedded Jetty Server**
1. Open a PowerShell terminal within the project directory.
2. Execute the build command:
   ```bash
   mvn clean package
   ```
3. Start the embedded Jetty server:
   ```bash
   mvn jetty:run
   ```
4. Access the API via: `http://localhost:8080/api/v1`

**B. Deployment on Apache Tomcat**
1. Build the project using:
   ```bash
   mvn clean package
   ```
2. Copy the generated `ROOT.war` file from the `target` directory into the Tomcat webapps directory (`%TOMCAT_HOME%\webapps`).
3. Restart or start the Tomcat server.
4. Access the deployed application at: `http://localhost:8080/api/v1`

**C. NetBeans Integrated Deployment**
1. Import the Maven project into the NetBeans IDE.
2. Ensure the project’s `finalName` is configured as `ROOT`, or set the context path to `/` via Project Properties.
3. Perform a clean build and deploy the project.
4. Start the application server and access the API at: `http://localhost:8080/api/v1`

## 4. API Usage Examples

The following examples demonstrate typical interactions with the API using curl commands:

Creating a Room (HTTP 201 Created)

curl -i -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library Quiet Study","capacity":40}'

Registering a Sensor (HTTP 201 Created)

curl -i -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0,"roomId":"LIB-301"}'

Retrieving Sensor List (HTTP 200 OK)

curl -i http://localhost:8080/api/v1/sensors

Submitting a Sensor Reading (HTTP 201 Created)

curl -i -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"R-001","timestamp":1710000000000,"value":23.5}'

Deleting a Sensor (HTTP 204 No Content)

curl -i -X DELETE http://localhost:8080/api/v1/sensors/TEMP-001

---

## 4. Report — Answers to Coursework Questions

---

### 🔹 Part 1: Foundation

#### Q1.1: Lifecycle of a JAX-RS Resource

Resource objects in JAX-RS have a default lifecycle scope which is request-based, which means that for every single request a new instance of the resource will be created.

In regards to our current project, due to the use of in-memory collections like HashMap, there was a need for data persistence through different requests. Therefore, we used a Singleton pattern approach (DataStore static field) to keep data across requests to ensure data created in previous requests will be available in future requests.

---

#### Q1.2: Advantages of HATEOAS

HATEOAS adds flexibility and convenience for APIs through the introduction of navigational links which make less likely that URLs would be hardcoded into the client application.

This allows clients to discover the next available actions by following the links, and thus makes it possible for an API to change its URL structure easily.

---

### 🔹 Part 2: Room Management

#### Q2.1: Identifier vs Full Object

Response size will be small due to returning only identifiers; therefore, this approach is better in terms of bandwidth.

On the other hand, the client will need to make additional requests to receive full information about the room, leading to an increase in the number of API requests (N+1 problem).

Returning a full object is better because, despite a slight increase in the amount of data being sent back, it allows fewer requests to be made and increases the performance of the application.

---

#### Q2.2: Idempotence of DELETE

DELETE operations can be said to be idempotent with regard to their result because the deletion will take place anyway irrespective of whether the request has been made once or several times.

However, the responses returned by these requests may differ; the first one will return **204 No Content**, while subsequent requests may return **404 Not Found**.

---

### 🔹 Part 3: Sensors & Filters

#### Q3.1: Non-JSON Payloads

Whenever a non-JSON payload is sent to an endpoint that only allows JSON payloads (`@Consumes(MediaType.APPLICATION_JSON)`), Jersey returns an error message indicating that the media type is unsupported (HTTP **415 Unsupported Media Type**).

Hence, this problem is handled automatically by the framework.

---

#### Q3.2: Query Parameters vs Path Segments

Query parameters should be preferred over path segments for filtering purposes because they are optional and flexible.

For example, `?type=CO2` clearly states the filter condition. On the other hand, path segments like `/sensors/CO2` are more suited for identifying resources rather than filtering them.

Using query parameters also allows future extensions, such as adding multiple filters (e.g., status or date).

---

### 🔹 Part 4: Sub-Resources

#### Q4.1: Advantages of Sub-Resource Locators

Sub-resource locators assist in making the code better organized through separation of concerns.

For example, the main resource first validates whether a sensor exists, and then delegates the request to a separate class such as `SensorReadingResource`.

This improves maintainability, readability, and scalability of the API.

---

### 🔹 Part 5: Filters and Security

#### Q5.1: JAX-RS Filters vs Logging in Methods

Filters offer a more appropriate way to handle logging compared to inserting logging statements throughout resource methods.

They handle cross-cutting concerns in a centralized location, ensuring there is no duplication of code.

This approach follows the DRY (Don't Repeat Yourself) principle and improves maintainability.

---

#### Q5.2: Potential Problems with Showing Stack Trace

It is highly dangerous to display stack traces to clients.

Stack traces may reveal sensitive internal details such as:

- Class names
- Libraries used
- File paths

This information can be exploited by attackers. Therefore, it is best practice to hide such details and return a generic error message instead.

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

Link-
