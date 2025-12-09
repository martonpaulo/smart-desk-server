package com.smartdesk.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "calendars")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Calendar extends Base {
    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false)
    private String name;

    private String description;
    private String url;
    private String color;
}
