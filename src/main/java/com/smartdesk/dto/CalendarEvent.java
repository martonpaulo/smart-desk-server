package com.smartdesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "A calendar event parsed from an ICS source")
public class CalendarEvent {
    @Schema(description = "Event UID", example = "1@example.com")
    private String uid;

    @Schema(description = "Event summary/title", example = "Team meeting")
    private String summary;

    @Schema(description = "Event description", example = "Discuss project status")
    private String description;

    @Schema(description = "Event start time (ISO-8601)", example = "2025-01-02T09:00:00Z")
    private OffsetDateTime start;

    @Schema(description = "Event end time (ISO-8601)", example = "2025-01-02T10:00:00Z")
    private OffsetDateTime end;

    @Schema(description = "Event location", example = "Office")
    private String location;

    @Schema(description = "Source URL where this event was fetched from", example = "https://example.com/mycalendar.ics")
    private String urlSource;

    public CalendarEvent() {}

    public CalendarEvent(String uid, String summary, String description, OffsetDateTime start, OffsetDateTime end, String location, String urlSource) {
        this.uid = uid;
        this.summary = summary;
        this.description = description;
        this.start = start;
        this.end = end;
        this.location = location;
        this.urlSource = urlSource;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public void setStart(OffsetDateTime start) {
        this.start = start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public void setEnd(OffsetDateTime end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }
}
