# Smart Desk API

Spring Boot WebFlux API for Smart Desk application.

## Features

- ✅ Add ICS calendars by URL
- ✅ Fetch and parse events from calendars
- ✅ Swagger UI documentation with examples
- ✅ JPA persistence with H2 database
- ✅ Functional routing style

## Swagger UI

Once the application is running, visit:
- Swagger UI: http://localhost:8080/swagger-ui.html

## H2 Database Console

For development, you can access the H2 database console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:smartdesk`
- Username: `sa`
- Password: (leave empty)

## Technology Stack

- Java 21
- Spring Boot 3.4.1
- Spring WebFlux (reactive)
- Spring Data JPA
- SpringDoc OpenAPI 2.7.0
- Lombok
