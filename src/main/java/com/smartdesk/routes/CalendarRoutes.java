package com.smartdesk.routes;

import com.smartdesk.handler.CalendarHandler;
import com.smartdesk.model.Calendar;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

public class CalendarRoutes {

    public RouterFunction<ServerResponse> calendarRoutes(CalendarHandler handler) {
        return route()
                .POST("/v1/calendars", accept(MediaType.APPLICATION_JSON), handler::addCalendar, ops -> ops
                        .operationId("addCalendar")
                        .summary("Add a calendar by source URL")
                        .description("Add a new ICS calendar by providing its source URL. Example: https://calendar.google.com/calendar/ical/en.usa%23holiday%40group.v.calendar.google.com/public/basic.ics")
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
                                .description("Invalid request or source URL")
                        )
                )
                .GET("/v1/calendars", accept(MediaType.APPLICATION_JSON), handler::getAllCalendars, ops -> ops
                        .operationId("getAllCalendars")
                        .summary("Get all calendars")
                        .description("Retrieve all calendars")
                        .tag("Calendars")
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.OK.value()))
                                .description("List of calendars")
                                .implementationArray(Calendar.class)
                        )
                )
                .build();
    }
}

