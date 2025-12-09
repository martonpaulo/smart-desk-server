package com.smartdesk.service;

import com.smartdesk.dto.CalendarEvent;
import com.smartdesk.repository.CalendarRepository;
import lombok.Data;
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
import java.util.Set;

@Service
public class CalendarService {
    private final CalendarRepository repository;
    private final HttpClient httpClient;

    public CalendarService(CalendarRepository repository) {
        this.repository = repository;
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public boolean addCalendarUrl(String url) {
        return repository.add(url);
    }

    @Data
    public static class EventsResult {
        public final List<CalendarEvent> events = new ArrayList<>();
        public final List<String> errors = new ArrayList<>();
    }

    public EventsResult getAllEvents() {
        EventsResult result = new EventsResult();
        Set<String> urls = repository.listAll();
        for (String url : urls) {
            try {
                List<CalendarEvent> events = fetchAndParse(url);
                result.events.addAll(events);
            } catch (Exception e) {
                result.errors.add(url + ": " + e.getMessage());
            }
        }
        return result;
    }

    private List<CalendarEvent> fetchAndParse(String url) throws IOException, InterruptedException {
        // support file:// URIs for tests
        if (url.startsWith("file:")) {
            URI uri = URI.create(url);
            try (InputStream is = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(uri))) {
                return parseIcs(is, url);
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() >= 400) {
            throw new IOException("HTTP " + response.statusCode());
        }

        try (InputStream is = response.body()) {
            return parseIcs(is, url);
        }
    }

    private List<CalendarEvent> parseIcs(InputStream is, String sourceUrl) throws IOException {
        List<CalendarEvent> out = new ArrayList<>();
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
                // parse eventLines
                out.add(parseEvent(eventLines, sourceUrl));
                continue;
            }
            if (inEvent) {
                // handle folded lines: lines that start with space or tab are continuation
                if (!eventLines.isEmpty() && (line.startsWith(" ") || line.startsWith("\t"))) {
                    int last = eventLines.size() - 1;
                    eventLines.set(last, eventLines.get(last) + line.substring(1));
                } else {
                    eventLines.add(line);
                }
            }
        }
        return out;
    }

    private CalendarEvent parseEvent(List<String> lines, String sourceUrl) {
        String uid = null;
        String summary = null;
        String description = null;
        String location = null;
        OffsetDateTime start = null;
        OffsetDateTime end = null;

        for (String l : lines) {
            String up = l;
            int idx = up.indexOf(':');
            if (idx <= 0) continue;
            String key = up.substring(0, idx);
            String value = up.substring(idx + 1);
            // key may have parameters like DTSTART;TZID=Europe/Paris
            String k = key.split(";")[0].toUpperCase();
            switch (k) {
                case "UID":
                    uid = value;
                    break;
                case "SUMMARY":
                    summary = value;
                    break;
                case "DESCRIPTION":
                    description = value;
                    break;
                case "LOCATION":
                    location = value;
                    break;
                case "DTSTART":
                    start = parseDateTime(value, key);
                    break;
                case "DTEND":
                    end = parseDateTime(value, key);
                    break;
                default:
                    // ignore
            }
        }

        return new CalendarEvent(uid, summary, description, start, end, location, sourceUrl);
    }

    private OffsetDateTime parseDateTime(String value, String key) {
        // value examples: 20250102T090000Z or 20250102
        try {
            if (value.endsWith("Z")) {
                Instant inst = Instant.from(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC).parse(value));
                return OffsetDateTime.ofInstant(inst, ZoneOffset.UTC);
            }
            // date-only
            if (value.matches("\\d{8}")) {
                LocalDate d = LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
                return d.atStartOfDay().atOffset(ZoneOffset.UTC);
            }
            // naive local date-time
            if (value.matches("\\d{8}T\\d{6}")) {
                LocalDateTime ldt = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
                return ldt.atOffset(ZoneOffset.UTC);
            }
        } catch (Exception e) {
            // fallback null
        }
        return null;
    }
}
