package com.smartdesk.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Component
public class HealthHandler {

    public Mono<ServerResponse> health(ServerRequest request) {
        return ServerResponse
                .ok()
                .bodyValue(Map.of(
                        "status", "ok",
                        "time", OffsetDateTime.now(ZoneOffset.UTC).toString()
                ));
    }
}

