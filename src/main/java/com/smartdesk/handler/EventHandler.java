package com.smartdesk.handler;

import com.smartdesk.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventHandler {
    private final EventService eventService;

    public Mono<ServerResponse> getEvents(ServerRequest request) {
        return ServerResponse
                .ok()
                .bodyValue(eventService.getAllEvents());
    }
}

