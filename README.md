# Smart Desk

This repository (folder name: `smart-desk-server`) contains the Smart Desk Spring Boot service.

- Public project name: Smart Desk
- Repository folder: `smart-desk-server`
- Java package and group ID: `com.smartdesk`

Spring Boot application with a single health endpoint at `/health` and a small calendar API:

- POST /calendars  — add a calendar by providing JSON { "url": "https://.../calendar.ics" }
- GET  /calendars/events — list parsed events from all added calendars

Swagger / OpenAPI UI

After starting the app (default port 8080) open the Swagger UI in your browser:

- http://localhost:8080/swagger-ui.html

Quick start (zsh)
```bash
./gradlew clean build
./gradlew bootRun
# then visit: http://localhost:8080/health
# Swagger UI: http://localhost:8080/swagger-ui.html
```

Example: add a calendar (POST)
```bash
curl -X POST "http://localhost:8080/calendars" \
  -H 'Content-Type: application/json' \
  -d '{"url":"file:///path/to/sample.ics"}'
```

Example: get events (GET)
```bash
curl http://localhost:8080/calendars/events
```
