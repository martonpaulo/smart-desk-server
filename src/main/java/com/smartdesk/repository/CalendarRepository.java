package com.smartdesk.repository;

import java.util.Set;

public interface CalendarRepository {
    boolean add(String url);
    Set<String> listAll();
}

