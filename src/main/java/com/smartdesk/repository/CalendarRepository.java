package com.smartdesk.repository;

import com.smartdesk.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByUid(String uid);
    Optional<Calendar> findByUrl(String url);
}

