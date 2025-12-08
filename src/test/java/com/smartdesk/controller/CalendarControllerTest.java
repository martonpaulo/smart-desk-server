package com.smartdesk.controller;

import com.smartdesk.dto.CalendarEvent;
import com.smartdesk.service.CalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(controllers = CalendarController.class)
public class CalendarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalendarService calendarService;

    @Test
    public void postAddsUrl() throws Exception {
        Mockito.when(calendarService.addCalendarUrl("https://example.com/calendar.ics")).thenReturn(true);

        mockMvc.perform(post("/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"https://example.com/calendar.ics\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void postDuplicateReturnsConflict() throws Exception {
        Mockito.when(calendarService.addCalendarUrl("https://example.com/calendar.ics")).thenReturn(false);

        mockMvc.perform(post("/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"https://example.com/calendar.ics\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getEventsReturnsEvents() throws Exception {
        CalendarService.EventsResult res = new CalendarService.EventsResult();
        res.events.add(new CalendarEvent("1", "s1", "d1", OffsetDateTime.now(), OffsetDateTime.now().plusHours(1), "loc", "u"));
        res.errors.add("http://bad.url: 404");
        Mockito.when(calendarService.getAllEvents()).thenReturn(res);

        mockMvc.perform(get("/calendars/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}

