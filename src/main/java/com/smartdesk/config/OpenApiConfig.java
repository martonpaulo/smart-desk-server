package com.smartdesk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Smart Desk API",
                version = "1.0"
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        }
)
@Configuration
public class OpenApiConfig {
}

