package com.smartdesk.config;

import com.smartdesk.handler.CalendarHandler;
import com.smartdesk.handler.EventHandler;
import com.smartdesk.handler.HealthHandler;
import com.smartdesk.model.Calendar;
import com.smartdesk.model.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler handler) {
        return route()
                .GET("/health", handler::health, ops -> ops
                        .operationId("health")
                        .summary("Health check")
                        .description("Check if the API is running")
                        .tag("Health")
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.OK.value()))
                                .description("API is healthy")
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> calendarRoutes(CalendarHandler handler) {
        return route()
                .POST("/api/v1/calendars", accept(MediaType.APPLICATION_JSON), handler::addCalendar, ops -> ops
                        .operationId("addCalendar")
                        .summary("Add a calendar by URL")
                        .description("Add a new ICS calendar by providing its URL. Example URL: https://calendar.google.com/calendar/ical/en.usa%23holiday%40group.v.calendar.google.com/public/basic.ics")
                        .tag("Calendars")
                        .requestBody(requestBodyBuilder()
                                .implementation(Calendar.class)
                        )
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.CREATED.value()))
                                .description("Calendar added successfully")
                                .implementation(Calendar.class)
                        )
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                                .description("Invalid request or URL")
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventRoutes(EventHandler handler) {
        return route()
                .GET("/api/v1/events", accept(MediaType.APPLICATION_JSON), handler::getEvents, ops -> ops
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



