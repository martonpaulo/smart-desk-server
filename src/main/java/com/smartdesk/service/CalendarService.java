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
        String url = calendar.getUrl();
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Calendar URL is required");
        }

        // Basic URL validation
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") ||
                    scheme.equalsIgnoreCase("https") ||
                    scheme.equalsIgnoreCase("file"))) {
                throw new IllegalArgumentException("URL must use http(s) or file");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + e.getMessage());
        }

        // Generate UID from URL if not provided
        if (calendar.getUid() == null || calendar.getUid().isBlank()) {
            calendar.setUid(generateUidFromUrl(url));
        }

        // Set default name from URL if not provided
        if (calendar.getName() == null || calendar.getName().isBlank()) {
            calendar.setName(extractNameFromUrl(url));
        }

        return calendarRepository.save(calendar);
    }

    private String generateUidFromUrl(String url) {
        return url.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
    }

    private String extractNameFromUrl(String url) {
        try {
            URI uri = new URI(url);
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
