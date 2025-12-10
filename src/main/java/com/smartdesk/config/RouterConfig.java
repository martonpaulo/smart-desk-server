package com.smartdesk.config;

import com.smartdesk.handler.CalendarHandler;
import com.smartdesk.handler.EventHandler;
import com.smartdesk.handler.HealthHandler;
import com.smartdesk.handler.IcsProxyHandler;
import com.smartdesk.routes.CalendarRoutes;
import com.smartdesk.routes.EventRoutes;
import com.smartdesk.routes.HealthRoutes;
import com.smartdesk.routes.IcsProxyRoutes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> calendarRoutes(CalendarHandler handler) {
        return new CalendarRoutes().calendarRoutes(handler);
    }

    @Bean
    public RouterFunction<ServerResponse> eventRoutes(EventHandler handler) {
        return new EventRoutes().eventRoutes(handler);
    }

    @Bean
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler handler) {
        return new HealthRoutes().healthRoutes(handler);
    }

    @Bean
    public RouterFunction<ServerResponse> icsProxyRoutes(IcsProxyHandler handler) {
        return new IcsProxyRoutes().icsProxyRoutes(handler);
    }
}

