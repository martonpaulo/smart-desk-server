package com.smartdesk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Event entity", example = """
    {
      "title": "Christmas Day",
      "description": "Federal holiday in the United States",
      "utcStartDateTime": "2025-12-25T00:00:00Z",
      "utcEndDateTime": "2025-12-26T00:00:00Z",
      "isAllDay": true,
      "isHidden": false,
      "isAlertDismissed": false,
      "calendarId": "550e8400-e29b-41d4-a716-446655440000",
      "calendarName": "US Holidays"
    }
    """)
public class Event {
    @Schema(description = "Event title", example = "Christmas Day", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Event description", example = "Federal holiday in the United States")
    private String description;

    @Schema(description = "Event start time in UTC", example = "2025-12-25T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant utcStartDateTime;

    @Schema(description = "Event end time in UTC", example = "2025-12-26T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant utcEndDateTime;

    @Schema(description = "Whether this is an all-day event", example = "true")
    private boolean isAllDay = false;

    @Schema(description = "Whether this event is hidden from the UI", example = "false")
    private boolean isHidden = false;

    @Schema(description = "Whether the alert for this event has been dismissed", example = "false")
    private boolean isAlertDismissed = false;

    @Schema(description = "ID of the calendar this event belongs to", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID calendarId;

    @Schema(description = "Name of the calendar this event belongs to", example = "US Holidays")
    private String calendarName;

    @Schema(description = "Color of the calendar this event belongs to", example = "#4285F4")
    private String calendarColor;
}
