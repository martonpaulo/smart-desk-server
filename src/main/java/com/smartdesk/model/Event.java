package com.smartdesk.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Event extends Base {
    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false)
    private String title;
    private String description;

    @Column(nullable = false)
    private Instant utcStartDateTime;
    @Column(nullable = false)
    private Instant utcEndDateTime;

    private boolean isAllDay = false;

    @Column(nullable = false)
    private boolean isAlertDismissed = false;

    private boolean isHidden = false;

    // Relationship to the Calendar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendarRef;
}
