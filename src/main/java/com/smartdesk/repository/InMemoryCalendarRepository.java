package com.smartdesk.repository;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class InMemoryCalendarRepository implements CalendarRepository {
    private final Set<String> urls = Collections.synchronizedSet(new LinkedHashSet<>());

    @Override
    public boolean add(String url) {
        return urls.add(url);
    }

    @Override
    public Set<String> listAll() {
        synchronized (urls) {
            return new LinkedHashSet<>(urls);
        }
    }
}

