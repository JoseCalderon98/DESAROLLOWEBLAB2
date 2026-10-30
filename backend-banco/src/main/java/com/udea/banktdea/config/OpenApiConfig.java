package com.udea.banktdea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI specification for Bank TdeA.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankTdeA - Transferencias Bancarias con QR API")
                        .description("API REST para traslado de dinero entre cuentas bancarias, histórico de transacciones y generación de código QR - Tecnológico de Antioquia")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecnológico de Antioquia")
                                .email("contacto@tdea.edu.co")
                                .url("https://www.tdea.edu.co"))
                        .license(new License().name("Apache 2.0")));
    }
}

