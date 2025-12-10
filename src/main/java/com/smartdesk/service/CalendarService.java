package com.smartdesk.service;

import com.smartdesk.model.Calendar;
import com.smartdesk.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public Calendar addCalendar(Calendar calendar) {
        String sourceUrl = calendar.getSourceUrl();
        if (sourceUrl == null || sourceUrl.isBlank()) {
            throw new IllegalArgumentException("Calendar source URL is required");
        }

        // Basic source URL validation
        try {
            URI uri = new URI(sourceUrl.trim());
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") ||
                    scheme.equalsIgnoreCase("https") ||
                    scheme.equalsIgnoreCase("file"))) {
                throw new IllegalArgumentException("Source URL must use http(s) or file");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid source URL: " + e.getMessage());
        }

        // Generate UID from source URL if not provided
        if (calendar.getUid() == null || calendar.getUid().isBlank()) {
            calendar.setUid(generateUidFromSourceUrl(sourceUrl));
        }

        // Set default name from source URL if not provided
        if (calendar.getName() == null || calendar.getName().isBlank()) {
            calendar.setName(extractNameFromSourceUrl(sourceUrl));
        }

        return calendarRepository.save(calendar);
    }

    private String generateUidFromSourceUrl(String sourceUrl) {
        return sourceUrl.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
    }

    private String extractNameFromSourceUrl(String sourceUrl) {
        try {
            URI uri = new URI(sourceUrl);
            String path = uri.getPath();
            if (path != null && !path.isEmpty()) {
                String[] parts = path.split("/");
                return parts[parts.length - 1].replace(".ics", "");
            }
            return uri.getHost();
        } catch (Exception e) {
            return "Calendar";
        }
    }
}
