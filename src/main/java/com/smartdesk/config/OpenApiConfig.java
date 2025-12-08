package com.smartdesk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Smart Desk Calendar API", version = "1.0", description = "Simple API to add ICS calendars and get events"))
@Configuration
public class OpenApiConfig {
}

