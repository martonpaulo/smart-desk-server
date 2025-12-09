package com.smartdesk.controller;

import com.smartdesk.dto.AddCalendarRequest;
import com.smartdesk.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService service;


    @Operation(summary = "Add an ICS calendar by URL")
    @PostMapping
    public ResponseEntity<?> addCalendar(@Valid @RequestBody AddCalendarRequest req) {
        String url = req.getUrl().trim();

        // Basic URL validation
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") ||
                                   scheme.equalsIgnoreCase("https") ||
                                   scheme.equalsIgnoreCase("file"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "URL must use http(s) or file"));
            }
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid URL"));
        }

        boolean added = service.addCalendarUrl(url);
        if (!added) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "URL already added"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get events from all added calendars")
    @GetMapping("/events")
    public ResponseEntity<Map<String, Object>> getEvents() {
        CalendarService.EventsResult res = service.getAllEvents();
        return ResponseEntity.ok(Map.of(
            "events", res.events,
            "errors", res.errors
        ));
    }
}
