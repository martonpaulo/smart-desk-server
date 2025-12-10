package com.smartdesk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "calendars")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Calendar entity", example = """
    {
      "name": "US Holidays",
      "description": "United States holidays calendar",
      "sourceUrl": "https://calendar.google.com/calendar/ical/en.usa%23holiday%40group.v.calendar.google.com/public/basic.ics",
      "color": "#4285F4"
    }
    """)
public class Calendar extends Base {
    @Column(nullable = false)
    @Schema(description = "Calendar name", example = "US Holidays", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Calendar description", example = "United States holidays calendar")
    private String description;

    @Column(nullable = false, unique = true)
    @Schema(description = "ICS calendar source URL", example = "https://calendar.google.com/calendar/ical/en.usa%23holiday%40group.v.calendar.google.com/public/basic.ics", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceUrl;

    @Schema(description = "Calendar color in hex format", example = "#4285F4")
    private String color;
}
