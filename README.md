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
