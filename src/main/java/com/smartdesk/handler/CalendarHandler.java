package com.smartdesk.handler;

import com.smartdesk.model.Calendar;
import com.smartdesk.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CalendarHandler {
    private final CalendarService calendarService;

    public Mono<ServerResponse> addCalendar(ServerRequest request) {
        return request.bodyToMono(Calendar.class)
                .flatMap(calendar -> {
                    Calendar result = calendarService.addCalendar(calendar);
                    return ServerResponse
                            .status(HttpStatus.CREATED)
                            .bodyValue(result);
                })
                .onErrorResume(e -> ServerResponse
                        .badRequest()
                        .bodyValue(Map.of("error", e.getMessage())));
    }

    public Mono<ServerResponse> getAllCalendars(ServerRequest request) {
        return ServerResponse
                .ok()
                .bodyValue(calendarService.getAllCalendars());
    }
}
