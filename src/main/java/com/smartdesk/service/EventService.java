package com.smartdesk.service;

import com.smartdesk.model.Calendar;
import com.smartdesk.model.Event;
import com.smartdesk.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final CalendarRepository calendarRepository;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public List<Event> getAllEvents() {
        List<Event> allEvents = new ArrayList<>();
        List<Calendar> calendars = calendarRepository.findAll();

        for (Calendar calendar : calendars) {
            try {
                List<Event> events = fetchAndParseEvents(calendar);
                allEvents.addAll(events);
            } catch (Exception e) {
                // Log error but continue with other calendars
                System.err.println("Error fetching events from " + calendar.getSourceUrl() + ": " + e.getMessage());
            }
        }

        return allEvents;
    }

    private List<Event> fetchAndParseEvents(Calendar calendar) throws IOException, InterruptedException {
        String sourceUrl = calendar.getSourceUrl();

        // Support file:// URIs for tests
        if (sourceUrl.startsWith("file:")) {
            URI uri = URI.create(sourceUrl);
            try (InputStream is = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(uri))) {
                return parseIcs(is, calendar);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sourceUrl))
                .GET()
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() >= 400) {
            throw new IOException("HTTP " + response.statusCode());
        }

        try (InputStream is = response.body()) {
            return parseIcs(is, calendar);
        }
    }

    private List<Event> parseIcs(InputStream is, Calendar calendar) throws IOException {
        List<Event> events = new ArrayList<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;
        boolean inEvent = false;
        List<String> eventLines = new ArrayList<>();

        while ((line = r.readLine()) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("BEGIN:VEVENT")) {
                inEvent = true;
                eventLines.clear();
                continue;
            }
            if (line.equalsIgnoreCase("END:VEVENT")) {
                inEvent = false;
                Event event = parseEvent(eventLines, calendar);
                if (event != null) {
                    events.add(event);
                }
                continue;
            }
            if (inEvent) {
                // Handle folded lines: lines that start with space or tab are continuation
                if (!eventLines.isEmpty() && (line.startsWith(" ") || line.startsWith("\t"))) {
                    int last = eventLines.size() - 1;
                    eventLines.set(last, eventLines.get(last) + line.substring(1));
                } else {
                    eventLines.add(line);
                }
            }
        }

        return events;
    }

    private Event parseEvent(List<String> lines, Calendar calendar) {
        String uid = null;
        String title = null;
        String description = null;
        Instant start = null;
        Instant end = null;
        boolean isAllDay = false;

        for (String l : lines) {
            int idx = l.indexOf(':');
            if (idx <= 0) continue;

            String key = l.substring(0, idx);
            String value = l.substring(idx + 1);
            String k = key.split(";")[0].toUpperCase();

            switch (k) {
                case "UID":
                    uid = value;
                    break;
                case "SUMMARY":
                    title = value;
                    break;
                case "DESCRIPTION":
                    description = value;
                    break;
                case "DTSTART":
                    ParsedDateTime parsed = parseDateTime(value, key);
                    start = parsed.instant;
                    isAllDay = parsed.isAllDay;
                    break;
                case "DTEND":
                    end = parseDateTime(value, key).instant;
                    break;
            }
        }

        if (uid == null || title == null || start == null || end == null) {
            return null; // Invalid event
        }

        Event event = new Event();
        event.setUid(uid);
        event.setTitle(title);
        event.setDescription(description);
        event.setUtcStartDateTime(start);
        event.setUtcEndDateTime(end);
        event.setAllDay(isAllDay);
        event.setAlertDismissed(false);
        event.setHidden(false);
        event.setCalendarRef(calendar);
        event.setCalendarId(calendar.getId());

        return event;
    }

    private static class ParsedDateTime {
        Instant instant;
        boolean isAllDay;

        ParsedDateTime(Instant instant, boolean isAllDay) {
            this.instant = instant;
            this.isAllDay = isAllDay;
        }
    }

    private ParsedDateTime parseDateTime(String value, String key) {
        try {
            if (value.endsWith("Z")) {
                // UTC time
                Instant inst = Instant.from(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                        .withZone(ZoneOffset.UTC)
                        .parse(value));
                return new ParsedDateTime(inst, false);
            }

            // Date-only (all-day event)
            if (value.matches("\\d{8}")) {
                LocalDate d = LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
                Instant inst = d.atStartOfDay().toInstant(ZoneOffset.UTC);
                return new ParsedDateTime(inst, true);
            }

            // Naive local date-time
            if (value.matches("\\d{8}T\\d{6}")) {
                LocalDateTime ldt = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
                Instant inst = ldt.toInstant(ZoneOffset.UTC);
                return new ParsedDateTime(inst, false);
            }
        } catch (Exception e) {
            // Fallback
        }

        // Default fallback
        return new ParsedDateTime(Instant.now(), false);
    }
}

