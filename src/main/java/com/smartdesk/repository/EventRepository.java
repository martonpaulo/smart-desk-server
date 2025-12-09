package com.smartdesk.repository;

import com.smartdesk.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByUid(String uid);
    List<Event> findByCalendarRefId(Long calendarId);
}

