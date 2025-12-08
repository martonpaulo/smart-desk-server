package com.smartdesk.dto;

import jakarta.validation.constraints.NotBlank;

public class AddCalendarRequest {
    @NotBlank
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
