package com.smartdesk.controller;

import com.smartdesk.dto.AddCalendarRequest;
import com.smartdesk.dto.CalendarEvent;
import com.smartdesk.service.CalendarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/calendars")
public class CalendarController {
    private final CalendarService service;

    public CalendarController(CalendarService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addCalendar(@Valid @RequestBody AddCalendarRequest req) {
        String url = req.getUrl();
        // basic validation
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "URL must use http or https"));
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

    @GetMapping("/events")
    public ResponseEntity<?> getEvents() {
        CalendarService.EventsResult res = service.getAllEvents();
        Map<String, Object> body = new HashMap<>();
        body.put("events", res.events);
        body.put("errors", res.errors);
        return ResponseEntity.ok(body);
    }
}

