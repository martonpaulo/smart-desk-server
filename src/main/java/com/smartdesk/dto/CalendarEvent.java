package com.smartdesk.dto;

import java.time.OffsetDateTime;

public class CalendarEvent {
    private String uid;
    private String summary;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private String location;
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

