package com.smartdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    private String uid;
    private String summary;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private String location;
    private String urlSource;
}
