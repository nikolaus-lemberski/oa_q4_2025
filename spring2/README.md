# Todo Application

A simple Spring Boot 2.7.x TODO REST API application using Java 17.

## Requirements

- Java 17
- Maven 3.6+

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Get all todos
```
GET http://localhost:8080/api/todos
```

### Get todo by ID
```
GET http://localhost:8080/api/todos/{id}
```

### Create todo
```
POST http://localhost:8080/api/todos
Content-Type: application/json

{
  "title": "My Todo",
  "description": "Description here",
  "completed": false
}
```

### Update todo
```
PUT http://localhost:8080/api/todos/{id}
Content-Type: application/json

{
  "title": "Updated Todo",
  "description": "Updated description",
  "completed": true
}
```

### Delete todo
```
DELETE http://localhost:8080/api/todos/{id}
```

## Example

```bash
# Create a todo
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Study Spring Boot 2.x","completed":false}'

# Get all todos
curl http://localhost:8080/api/todos
```

