package com.smartdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Event entity", example = """
    {
      "id": 1,
      "uid": "holiday-2025-christmas@google.com",
      "title": "Christmas Day",
      "description": "Federal holiday in the United States",
      "utcStartDateTime": "2025-12-25T00:00:00Z",
      "utcEndDateTime": "2025-12-26T00:00:00Z",
      "isAllDay": true,
      "isAlertDismissed": false,
      "isHidden": false
    }
    """)
public class Event extends Base {
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique identifier for the event", example = "holiday-2025-christmas@google.com")
    private String uid;

    @Column(nullable = false)
    @Schema(description = "Event title", example = "Christmas Day", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Event description", example = "Federal holiday in the United States")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Event start time in UTC", example = "2025-12-25T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant utcStartDateTime;

    @Column(nullable = false)
    @Schema(description = "Event end time in UTC", example = "2025-12-26T00:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant utcEndDateTime;

    @Schema(description = "Whether this is an all-day event", example = "true")
    private boolean isAllDay = false;

    @Column(nullable = false)
    @Schema(description = "Whether the alert has been dismissed", example = "false")
    private boolean isAlertDismissed = false;

    @Schema(description = "Whether the event is hidden", example = "false")
    private boolean isHidden = false;

    // Relationship to the Calendar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    @JsonIgnore
    @Schema(hidden = true)
    private Calendar calendarRef;
}
