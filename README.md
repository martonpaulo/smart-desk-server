# Smart Desk Calendar Server

Simple Spring Boot service to add ICS calendar URLs and retrieve events.

## Features

- **In-memory storage** - No database required
- **POST /calendars** - Add a calendar by URL
- **GET /calendars/events** - Get all events from added calendars
- **Simple ICS parser** - No external calendar libraries

## Quick Start

```bash
# Build and run
./gradlew bootRun

# Visit Swagger UI
open http://localhost:8080/swagger-ui.html
```

## API Usage

### Add a calendar

```bash
curl -X POST http://localhost:8080/calendars \
  -H 'Content-Type: application/json' \
  -d '{"url":"https://example.com/mycalendar.ics"}'
```

### Get all events

```bash
curl http://localhost:8080/calendars/events
```

Response:
```json
{
  "events": [
    {
      "uid": "1@example.com",
      "summary": "Team Meeting",
      "description": "Weekly sync",
      "start": "2025-01-02T09:00:00Z",
      "end": "2025-01-02T10:00:00Z",
      "location": "Office",
      "urlSource": "https://example.com/mycalendar.ics"
    }
  ],
  "errors": []
}
```

## Testing

```bash
./gradlew test
```

