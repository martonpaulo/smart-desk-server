package com.smartdesk.routes;

import com.smartdesk.handler.EventHandler;
import com.smartdesk.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

public class EventRoutes {

    public RouterFunction<ServerResponse> eventRoutes(EventHandler handler) {
        return route()
                .GET("/v1/events", accept(MediaType.APPLICATION_JSON), handler::getEvents, ops -> ops
                        .operationId("getEvents")
                        .summary("Get all events")
                        .description("Retrieve all events from all added calendars")
                        .tag("Events")
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.OK.value()))
                                .description("List of events")
                                .implementationArray(Event.class)
                        )
                )
                .build();
    }
}

