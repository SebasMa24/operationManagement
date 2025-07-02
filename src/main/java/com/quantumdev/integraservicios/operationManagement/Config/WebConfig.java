package com.quantumdev.integraservicios.operationManagement.Config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class WebConfig {
    
    /** El entorno para acceder a las propiedades de configuración */
    private final Environment environment;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configurar orígenes permitidos
        String frontendUrl = environment.getProperty("frontend.url");
        if (frontendUrl != null && !frontendUrl.isEmpty()) {
            configuration.setAllowedOrigins(List.of(frontendUrl));
        } else {
            // Configuración por defecto para desarrollo
            configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));
        }
        
        // Métodos HTTP para endpoints de hardware y espacios
        configuration.setAllowedMethods(Arrays.asList(
            "GET",      // Para consultas, availability, user reservations
            "POST",     // Para crear reservas, handOver, return
            "DELETE",   // Para eliminar reservas
            "OPTIONS"   // Para preflight requests
        ));
        
        // Headers necesarios para JWT y operaciones
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",     // Para tokens JWT
            "Content-Type",      // Para requests JSON
            "Accept"            // Para respuestas
        ));
        
        // Permitir credenciales para JWT
        configuration.setAllowCredentials(true);
        
        // Crear source para rutas específicas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Aplicar a endpoints de operaciones
        source.registerCorsConfiguration("/api/operations/**", configuration);
        
        return source;
    }
}