package com.smartdesk.routes;

import com.smartdesk.handler.HealthHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

public class HealthRoutes {

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
}

