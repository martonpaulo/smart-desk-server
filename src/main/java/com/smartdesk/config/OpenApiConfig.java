package com.smartdesk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Smart Desk Calendar API",
                version = "1.0",
                description = """
                        Simple API to manage calendars and events.

                        **Features:**
                        - Add ICS calendars by URL
                        - Fetch and parse events from calendars

                        **Example ICS URL:**
                        https://calendar.google.com/calendar/ical/en.usa%23holiday%40group.v.calendar.google.com/public/basic.ics
                        """
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        }
)
@Configuration
public class OpenApiConfig {
}

