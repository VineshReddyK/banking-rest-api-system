package com.vinesh.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking REST API System")
                        .description("Spring Boot backend API for user authentication, account management, and banking transactions.")
                        .version("1.0.0"));
    }
}
