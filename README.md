API Design Summary
Base URL: /api/v1 (configured using @ApplicationPath in the JAX-RS setup).
Available Resources:
Rooms: /rooms — Provides full CRUD operations for Room entities (fields: id, name, capacity).
Sensors: /sensors — Supports CRUD operations for Sensor entities (fields: id, type, status, currentValue, roomId). Note that the sensor ID cannot be modified once created.
Readings: /sensors/{sensorId}/readings — A nested resource handling SensorReading entities (fields: id, timestamp, value). Creating a reading also updates the currentValue of the associated sensor.
Data Storage: Uses an in-memory DataStore, meaning all data will be cleared when the server restarts.
Error Handling: Returns standard HTTP responses such as:
400 Bad Request
404 Not Found
409 Conflict
422 Unprocessable Entity
403 Forbidden (for rule violations)
Build and Run Instructions

Requirements: Java 11 or higher and Maven installed.
Default URL: http://localhost:8080

Option A: Run using Embedded Jetty
Open PowerShell in the project directory.
Execute: mvn clean package
Start the server: mvn jetty:run
Access the API at: http://localhost:8080/api/v1
Option B: Deploy on External Tomcat
Run: mvn clean package
Copy the generated target/ROOT.war file into %TOMCAT_HOME%\webapps\ROOT.war
Restart or start the Tomcat server.
Open: http://localhost:8080/api/v1
Option C: Use NetBeans with Built-in Tomcat
Import the Maven project into NetBeans.
Ensure the finalName is set to ROOT, or configure the context path as / via Project Properties → Run.
Build and deploy the project, then start the server.
Navigate to: http://localhost:8080/api/v1
Sample curl Commands (PowerShell Compatible)

Create a Room (201 Created)

curl -i -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":40}'

Add a Sensor to the Room (201 Created)

curl -i -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0,"roomId":"LIB-301"}'

Retrieve All Sensors (200 OK)

curl -i http://localhost:8080/api/v1/sensors

Insert a Sensor Reading (201 Created)

curl -i -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d '{"id":"R-001","timestamp":1710000000000,"value":23.5}'

Remove a Sensor (204 No Content)

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
