package com.smartdesk.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.smartdesk.model.Calendar;
import com.smartdesk.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IcsProxyService {

    private final CalendarRepository calendarRepository;
    private final RestTemplate restTemplate;

    /**
     * Fetches and filters a calendar by ID using Biweekly library.
     * Biweekly handles Windows Timezones (e.g., "E. South America Standard Time") automatically.
     */
    public String getFilteredCalendar(UUID calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new CalendarNotFoundException("Calendar not found: " + calendarId));

        String sourceUrl = calendar.getSourceUrl();
        log.info("Fetching calendar from source: {}", sourceUrl);

        String originalIcs = fetchCalendarFromSource(sourceUrl);

        return filterCalendar(originalIcs);
    }

    private String fetchCalendarFromSource(String sourceUrl) {
        try {
            String icsContent = restTemplate.getForObject(sourceUrl, String.class);
            if (icsContent == null || icsContent.isEmpty()) {
                throw new CalendarFetchException("Empty response from source URL: " + sourceUrl);
            }
            return icsContent;
        } catch (Exception e) {
            log.error("Failed to fetch calendar from source URL: {}", sourceUrl, e);
            throw new CalendarFetchException("Failed to fetch calendar from source URL", e);
        }
    }

    private String filterCalendar(String icsContent) {
        try {
            ICalendar ical = Biweekly.parse(icsContent).first();

            if (ical == null) {
                log.warn("Parsed content resulted in null calendar. Returning empty string.");
                return "";
            }

            // Remove events starting with "Canceled: "
            ical.getEvents().removeIf(event -> {
                String summary = getSummaryValue(event);
                boolean isCanceled = summary != null && summary.startsWith("Canceled: ");

                if (isCanceled) {
                    log.debug("Filtering out canceled event: {}", summary);
                }
                return isCanceled;
            });

            log.info("Calendar filtered successfully. Remaining events: {}", ical.getEvents().size());

            // Serialize back to String
            return Biweekly.write(ical).go();

        } catch (Exception e) {
            log.error("Error parsing or filtering calendar with Biweekly", e);
            throw new CalendarParsingException("Failed to parse calendar content", e);
        }
    }

    /**
     * Helper to safely extract summary value from an event
     */
    private String getSummaryValue(VEvent event) {
        if (event.getSummary() != null) {
            return event.getSummary().getValue();
        }
        return null;
    }

    // Custom Exceptions
    public static class CalendarNotFoundException extends RuntimeException {
        public CalendarNotFoundException(String message) { super(message); }
    }

    public static class CalendarFetchException extends RuntimeException {
        public CalendarFetchException(String message) { super(message); }
        public CalendarFetchException(String message, Throwable cause) { super(message, cause); }
    }

    public static class CalendarParsingException extends RuntimeException {
        public CalendarParsingException(String message, Throwable cause) { super(message, cause); }
    }
}
