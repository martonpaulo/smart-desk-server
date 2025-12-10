package com.smartdesk.routes;

import com.smartdesk.handler.IcsProxyHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

public class IcsProxyRoutes {

    public RouterFunction<ServerResponse> icsProxyRoutes(IcsProxyHandler handler) {
        return route()
                .GET("/v1/ics-proxy/{id}", handler::getFilteredCalendar, ops -> ops
                        .operationId("getFilteredIcs")
                        .summary("Get filtered ICS calendar")
                        .description("Returns a filtered ICS calendar file that excludes events with summary starting with 'Canceled: '")
                        .tag("ICS Proxy")
                        .parameter(parameterBuilder()
                                .in(ParameterIn.PATH)
                                .name("id")
                                .description("Calendar ID")
                                .required(true)
                                .example("UUID-STRING-HERE")
                        )
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.OK.value()))
                                .description("ICS calendar file")
                        )
                        .response(responseBuilder()
                                .responseCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                                .description("Calendar not found")
                        )
                )
                .build();
    }
}

