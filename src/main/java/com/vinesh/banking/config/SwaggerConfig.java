package com.vinesh.banking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI bankingOpenAPI() {
        Info apiInfo = new Info()
                .title("Banking REST API System")
                .description("Production-ready Banking REST API — user authentication, account management, and transactions.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Vinesh Reddy Kankanalapally")
                        .url("https://github.com/VineshReddyK"));

        SecurityScheme jwtScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(apiInfo)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, jwtScheme));
    }
}
