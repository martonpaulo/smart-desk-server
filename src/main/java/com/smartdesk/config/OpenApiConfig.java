package com.smartdesk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Smart Desk Calendar API", version = "0.1.0", description = "APIs to add ICS calendars and list events", contact = @Contact(name = "SmartDesk"), license = @License(name = "MIT")))
@Configuration
public class OpenApiConfig {
}

