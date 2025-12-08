package com.smartdesk.service;

import com.smartdesk.repository.InMemoryCalendarRepository;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarServiceIntegrationTest {
    @Test
    public void parsesSampleIcsFromFile() throws URISyntaxException {
        InMemoryCalendarRepository repo = new InMemoryCalendarRepository();
        CalendarService service = new CalendarService(repo);
        String path = Paths.get(getClass().getClassLoader().getResource("sample.ics").toURI()).toUri().toString();
        assertTrue(service.addCalendarUrl(path));
        CalendarService.EventsResult res = service.getAllEvents();
        assertTrue(res.errors.isEmpty(), String.join(";", res.errors));
        assertEquals(2, res.events.size());
        // check first event summary
        assertEquals("Test Event 1", res.events.stream().filter(e -> "1@example.com".equals(e.getUid())).findFirst().get().getSummary());
    }
}
