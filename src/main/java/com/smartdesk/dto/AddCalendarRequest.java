package com.smartdesk.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request to add a calendar by URL.
 */
public class AddCalendarRequest {
    @NotBlank
    @Schema(description = "URL of the .ics calendar to add", example = "https://example.com/mycalendar.ics")
    private String url;

    public AddCalendarRequest() {}

    public AddCalendarRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
