package com.udea.apptdea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 / Swagger documentation configuration.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Builds and registers the OpenAPI definition bean.
     *
     * @return Configured OpenAPI specification metadata
     */
    @Bean
    public OpenAPI apptdeaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AppTdeA - Reservation Service API")
                        .description("API REST para el Sistema de Gestión de Reservas - Tecnológico de Antioquia 2026")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecnológico de Antioquia")
                                .email("contacto@tdea.edu.co")
                                .url("https://www.tdea.edu.co"))
                        .license(new License().name("Apache 2.0")));
    }
}
