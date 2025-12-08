package com.smartdesk.controller;

import com.smartdesk.dto.AddCalendarRequest;
import com.smartdesk.dto.CalendarEvent;
import com.smartdesk.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService service;

    public CalendarController(CalendarService service) {
        this.service = service;
    }

    @Operation(summary = "Add an ICS calendar by URL",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Calendar added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = Map.class), examples = @ExampleObject("{\"error\": \"Invalid URL\"}"))),
                    @ApiResponse(responseCode = "409", description = "Already exists", content = @Content(schema = @Schema(implementation = Map.class), examples = @ExampleObject("{\"error\": \"URL already added\"}")))
            })
    @PostMapping
    public ResponseEntity<?> addCalendar(@Valid @RequestBody AddCalendarRequest req) {
        String url = req.getUrl();
        // basic validation
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("file"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "URL must use http(s) or file"));
            }
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid URL"));
        }

        boolean added = service.addCalendarUrl(url.trim());
        if (!added) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "URL already added"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get events from all added calendars",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Aggregated events and errors",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    description = "Response with events and errors"
                            ), examples = @ExampleObject(value = "{\"events\": [{\"uid\": \"1@example.com\", \"summary\": \"Test Event 1\", \"start\": \"2025-01-02T09:00:00Z\", \"end\": \"2025-01-02T10:00:00Z\", \"location\": \"Office\", \"urlSource\": \"https://example.com/mycalendar.ics\"}], \"errors\": [] }")))
            })
    @GetMapping("/events")
    public ResponseEntity<?> getEvents() {
        CalendarService.EventsResult res = service.getAllEvents();
        Map<String, Object> body = new HashMap<>();
        body.put("events", res.events);
        body.put("errors", res.errors);
        return ResponseEntity.ok(body);
    }
}
