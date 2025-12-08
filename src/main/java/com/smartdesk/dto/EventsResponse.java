package com.smartdesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing aggregated calendar events and parsing errors")
public class EventsResponse {
    @Schema(description = "Parsed events from all added calendars")
    private List<CalendarEvent> events;

    @Schema(description = "Errors encountered per URL (if any)", example = "[\"https://bad.example.com: HTTP 500\"]")
    private List<String> errors;

    public EventsResponse() {}

    public EventsResponse(List<CalendarEvent> events, List<String> errors) {
        this.events = events;
        this.errors = errors;
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

