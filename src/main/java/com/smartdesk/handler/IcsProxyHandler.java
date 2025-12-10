package com.smartdesk.handler;

import com.smartdesk.service.IcsProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class IcsProxyHandler {

    private final IcsProxyService icsProxyService;

    public Mono<ServerResponse> getFilteredCalendar(ServerRequest request) {
        String idStr = request.pathVariable("id");

        return Mono.just(idStr)
            .map(UUID::fromString)

            .flatMap(uuid -> Mono.fromCallable(() -> icsProxyService.getFilteredCalendar(uuid))
                .subscribeOn(Schedulers.boundedElastic())
            )

            .flatMap(icsContent -> ServerResponse.ok()
                .contentType(MediaType.parseMediaType("text/calendar; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"calendar.ics\"")
                .bodyValue(icsContent)
            )

            .onErrorResume(IllegalArgumentException.class, e -> {
                log.warn("Invalid UUID format received: '{}'", idStr);
                return ServerResponse.badRequest().build();
            })

            .onErrorResume(IcsProxyService.CalendarNotFoundException.class, e -> {
                log.warn("Calendar not found: {}", e.getMessage());
                return ServerResponse.notFound().build();
            })

            .onErrorResume(Exception.class, e -> {
                log.error("Unexpected error processing calendar request", e);
                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            });
    }
}
