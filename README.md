# Smart Campus Sensor and Room Management API

This project was developed for the 5COSC022W Client-Server Architectures coursework. It is a RESTful API built using Java, JAX-RS, Jersey, Maven, and the Grizzly HTTP server. The system manages rooms, sensors assigned to rooms, and sensor readings. All data is stored in memory using Java collections, in line with the coursework requirement not to use a database.

## API Design Overview

The API is organised around three main resources:

- Rooms
- Sensors
- Sensor Readings

A room can contain multiple sensors.  
A sensor belongs to one room.  
A sensor can also contain multiple readings.

The API follows a resource-based structure:

- `/api/v1/` provides the root discovery information
- `/api/v1/rooms` manages rooms
- `/api/v1/sensors` manages sensors
- `/api/v1/sensors/{id}/readings` manages readings belonging to a specific sensor

The design also includes:

- filtering sensors by type using query parameters
- validation for invalid requests
- custom exception handling
- HTTP status code mapping
- request and response logging using a JAX-RS filter

## Technologies Used

- Java 17
- Maven
- JAX-RS
- Jersey
- Grizzly HTTP Server
- Jackson JSON support
- NetBeans

## Project Structure

```text
src/main/java/com/smartcampus/api
└── Main.java
└── DataStore.java
├── config
│   ├── AppConfig.java
│
├── model
│   ├── Room.java
│   ├── Sensor.java
│   ├── SensorReading.java
│   └── ErrorResponse.java
├── resource
│   ├── RootResource.java
│   ├── RoomResource.java
│   ├── SensorResource.java
│   └── SensorReadingResource.java
│  
├── exception
│   ├── RoomNotEmptyException.java
│   ├── LinkedResourceNotFoundException.java
│   ├── SensorUnavailableException.java
│   ├── RoomNotEmptyExceptionMapper.java
│   ├── LinkedResourceNotFoundExceptionMapper.java
│   ├── SensorUnavailableExceptionMapper.java
│   └── GenericExceptionMapper.java
└── filter
    └── LoggingFilter.java
```

## How to Build and Run the Project

### Prerequisites

Before running the project, make sure the following are installed on your machine:

- Java 17 or later
- Maven
- NetBeans or another Java IDE

You can check the installed versions using:

```bash
java -version
mvn -version
```

### Step 1: Clone the Repository

```bash
git clone https://github.com/Thinal3/Smart-Campus-Sensor-and-Room-Management-API.git
cd Smart-Campus-Sensor-and-Room-Management-API
```

### Step 2: Open the Project

Open the project folder in NetBeans.

### Step 3: Reload Maven Dependencies

In NetBeans:

- Right click the project
- Select `Reload Project`

### Step 4: Clean and Build the Project

In NetBeans:

- Right click the project
- Select `Clean and Build`

### Step 5: Run the Server

Run the `Main.java` file.

The server will start at:

```text
http://localhost:8080/api/v1/
```

### Step 6: Verify the Server

Open the following URL in a browser or Postman:

```text
http://localhost:8080/api/v1/
```

If the project is running correctly, the API will return the root discovery response in JSON format.

## Main Endpoints

### Root Endpoint

- `GET /api/v1/`

### Room Management

- `GET /api/v1/rooms`
- `POST /api/v1/rooms`
- `GET /api/v1/rooms/{id}`
- `DELETE /api/v1/rooms/{id}`

### Sensor Management

- `GET /api/v1/sensors`
- `GET /api/v1/sensors?type=CO2`
- `POST /api/v1/sensors`
- `GET /api/v1/sensors/{id}`

### Sensor Readings

- `GET /api/v1/sensors/{id}/readings`
- `POST /api/v1/sensors/{id}/readings`

## Sample curl Commands

The following examples assume the server is already running on:

```text
http://localhost:8080
```

### 1. Get API root information

```bash
curl -X GET http://localhost:8080/api/v1/
```

### 2. Create a room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"R100\",\"name\":\"Main Library\",\"capacity\":120}"
```

### 3. Get all rooms

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

### 4. Get one room by ID

```bash
curl -X GET http://localhost:8080/api/v1/rooms/R100
```

### 5. Create a sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"S100\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":400,\"roomId\":\"R100\"}"
```

### 6. Filter sensors by type

```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"
```

### 7. Get one sensor by ID

```bash
curl -X GET http://localhost:8080/api/v1/sensors/S100
```

### 8. Add a sensor reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/S100/readings \
-H "Content-Type: application/json" \
-d "{\"id\":\"RD100\",\"timestamp\":1713700000,\"value\":480}"
```

### 9. Get readings for a sensor

```bash
curl -X GET http://localhost:8080/api/v1/sensors/S100/readings
```

### 10. Delete a room

```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/R100
```

## Example Request Bodies

### Room

```json
{
  "id": "R100",
  "name": "Main Library",
  "capacity": 120
}
```

### Sensor

```json
{
  "id": "S100",
  "type": "CO2",
  "status": "ACTIVE",
  "currentValue": 400,
  "roomId": "R100"
}
```

### Sensor Reading

```json
{
  "id": "RD100",
  "timestamp": 1713700000,
  "value": 480
}
```

## Error Handling

The API returns structured JSON error responses for invalid operations.

Examples include:

- `400 Bad Request` for missing or invalid input
- `403 Forbidden` when adding a reading to a sensor that is not active
- `404 Not Found` when a resource does not exist
- `409 Conflict` when attempting to create a duplicate resource or delete a room that still contains sensors
- `422 Unprocessable Entity` when trying to create a sensor for a room that does not exist
- `500 Internal Server Error` for unexpected server-side failures

## Notes

- The system uses in-memory storage only
- No database is used
- All stored data is lost when the server is restarted
- The project follows the coursework requirement to use JAX-RS rather than Spring Boot

## Conceptual Report Answers

### 1. Default lifecycle of a JAX-RS Resource class

A JAX-RS resource class is, by default, instantiated for each request separately, and is not treated as a singleton. This means a new resource object is created for every incoming HTTP request.

This affects the way in-memory data should be handled. Since normal instance fields in the resource class exist only for the lifetime of that request object, shared data such as rooms, sensors, and readings should be stored in a separate common data store rather than inside the resource class itself.

Even though each client gets a separate resource instance, the shared in-memory data can still be accessed by multiple clients at the same time. This can lead to inconsistent data or race conditions if updates happen concurrently. Because of this, shared in-memory data should be managed in a thread-safe way.

### 2. Why hypermedia is considered a hallmark of advanced RESTful design

Hypermedia is considered a feature of a well-designed REST API because it makes the API more self-descriptive. Instead of only returning data, the server can also return links to related resources and possible next actions. This means the client can understand how to interact with the API by following the information included in the response.

This benefits client developers because they do not have to rely only on static API documentation. With static documentation, developers must already know the resource structure, the connections between resources, and the possible actions that can be performed. If the API changes later, the documentation and the client code may become outdated.

Hypermedia makes the API easier to explore and reduces the amount of hardcoded knowledge needed on the client side. As a result, the client can interact with the API in a more flexible and guided way.

### 3. Returning only room IDs versus returning full room objects

Returning only room IDs makes the response smaller, which reduces bandwidth usage and can be useful when there are many rooms and only a simple reference list is needed.

However, returning only IDs usually creates more work for the client. If the client later needs more information such as the room name, capacity, or linked sensors, it must send additional requests for each room. This increases both client-side processing and the total number of network requests.

Returning full room objects makes the response larger, but it gives the client all the necessary information at once. This reduces the need for follow-up requests and makes the client logic simpler. Therefore, IDs are more lightweight, while full objects are often more practical.

### 4. Whether DELETE is idempotent in this implementation

The DELETE operation is idempotent in this implementation.

If a client sends a DELETE request for a room and the room exists, the first request removes it successfully. If the same DELETE request is sent again, the room is already gone, so the second request does not make any further change to the system. The final state remains the same because the room is still absent.

The response may differ between requests. For example, the first request may return success, while later requests may return `404 Not Found`. However, idempotency is about whether the repeated request changes the server state again. In this case, no additional change happens after the first successful deletion.

### 5. Consequences of sending data in a different format from `application/json`

Using `@Consumes(MediaType.APPLICATION_JSON)` tells JAX-RS that the method only accepts JSON request bodies. If a client sends data in another format such as `text/plain` or `application/xml`, the request will not match the media type expected by the method.

In such a case, JAX-RS stops the request before the method body is executed and usually returns HTTP `415 Unsupported Media Type`. This means the framework itself detects the mismatch between the request format and the expected content type.

This is important because it protects the API contract. The client must send the data in JSON format with the correct `Content-Type` header. Requests sent in unsupported formats are automatically rejected.

### 6. Why the query parameter approach is generally considered superior for filtering and searching collections

Using a query parameter such as `/api/v1/sensors?type=CO2` is usually better because it clearly shows that the client is still requesting the main collection of sensors, but with a filter applied to it.

If the type is placed directly in the path, such as `/api/v1/sensors/type/CO2`, it can make the API look like it is exposing a completely different resource instead of simply filtering the existing collection. This can make the path structure more complicated, especially when more filters need to be added.

Query parameters are easier to extend and combine. For example, the API can later support filtering such as `?type=CO2&status=ACTIVE` without changing the main path structure. For that reason, query parameters are generally the cleaner and more flexible choice for filtering and searching collections.

### 7. Architectural benefits of the Sub-Resource Locator pattern

The Sub-Resource Locator pattern helps organise an API by splitting nested functionality into smaller and more focused classes. Instead of defining all endpoints in one large controller, the logic for related sub-resources can be delegated to dedicated classes.

For example, in a path such as `sensors/{id}/readings`, sensor-related operations can remain in the sensor resource class, while reading-related operations can be handled in a separate reading resource class. This makes the code easier to read and allows each class to focus on one responsibility.

This approach becomes more useful as the API grows. If every nested path were placed inside one large controller, the file would become crowded and harder to manage. By dividing responsibilities into smaller classes, the API becomes easier to maintain, test, and extend.

### 8. Why HTTP 422 is often more accurate than 404 for a missing reference in a valid JSON body

HTTP `422 Unprocessable Entity` is often more appropriate when the JSON request body is valid, but the meaning of the data inside it is incorrect. For example, a client may send a valid sensor object, but the `roomId` inside it may refer to a room that does not exist.

In contrast, HTTP `404 Not Found` usually means that the requested URL or resource path itself does not exist. In this case, the endpoint is correct and available, so the issue is not the path but the invalid reference inside the payload.

For this reason, `422` is semantically more accurate. It shows that the server understood the request and its format, but could not process it because of a logical validation error in the request content.

### 9. Risks of exposing internal Java stack traces to external users

Exposing internal Java stack traces to external users can create security risks because they reveal details about the internal structure of the application. A stack trace may contain package names, class names, method names, file names, and line numbers where the error happened.

A malicious user could use this information to learn more about the architecture of the application and identify possible weak points. In some cases, stack traces may also expose server paths, configuration details, or information related to the database and backend logic.

For this reason, it is safer to return only controlled and limited error messages to end users, while keeping the full stack trace available only in server-side logs for developers.

### 10. Why JAX-RS filters are better for logging than manual `Logger.info()` calls everywhere

Using JAX-RS filters for logging is better because logging is a cross-cutting concern that affects many endpoints in the same way. If `Logger.info()` is added manually inside every resource method, the same code has to be repeated many times, which makes the codebase harder to maintain.

Filters allow logging to be handled in one central place. Every request and response passes through the filter, so the logging behaviour stays consistent across the whole API. This also keeps the resource methods cleaner because they can focus only on business logic.

Another advantage is that the logging system becomes easier to update. If the logging format or behaviour needs to change later, only the filter class has to be modified instead of editing every individual resource method.


