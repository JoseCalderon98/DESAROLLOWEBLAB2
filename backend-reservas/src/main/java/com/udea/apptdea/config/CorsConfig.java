package com.udea.apptdea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Global Cross-Origin Resource Sharing (CORS) filter configuration.
 * Enables the Angular (http://localhost:4200) and React (http://localhost:3000)
 * frontends to securely communicate with the REST API.
 */
@Configuration
public class CorsConfig {

    /**
     * Registers the CorsFilter bean covering all endpoints (/**).
     *
     * @return CorsFilter configured with allowed origins, headers, and HTTP methods
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization", "Link", "X-Total-Count"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
