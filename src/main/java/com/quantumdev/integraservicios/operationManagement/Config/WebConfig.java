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
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig {
    
    /** El entorno para acceder a las propiedades de configuración */
    private final Environment environment;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configurar orígenes permitidos
        String frontendUrl = environment.getProperty("frontend.url");
        
        if (frontendUrl != null && !frontendUrl.isEmpty()) {
            // Limpiar la URL si tiene barra final
            String cleanUrl = frontendUrl.endsWith("/") ? 
                frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
            
            configuration.setAllowedOrigins(List.of(cleanUrl));
            log.info("CORS configurado para frontend: {}", cleanUrl);
        } else {
            // Configuración por defecto para desarrollo local
            configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "https://localhost:*"
            ));
            log.warn("frontend.url no configurado, usando patrones de desarrollo local");
        }
        
        // Métodos HTTP para endpoints de hardware y espacios
        configuration.setAllowedMethods(Arrays.asList(
            "GET",      // Para consultas, availability, user reservations
            "POST",     // Para crear reservas, handOver, return
            "PUT",      // Para actualizar reservas/estados
            "DELETE",   // Para eliminar reservas
            "PATCH",    // Para actualizaciones parciales
            "OPTIONS"   // Para preflight requests
        ));
        
        // Headers necesarios para JWT y operaciones
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",     // Para tokens JWT
            "Content-Type",      // Para requests JSON
            "Accept",           // Para respuestas
            "X-Requested-With", // Para AJAX requests
            "Cache-Control"     // Para control de cache
        ));
        
        // Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition"
        ));
        
        // Permitir credenciales para JWT
        configuration.setAllowCredentials(true);
        
        // Tiempo de cache para preflight requests (en segundos)
        configuration.setMaxAge(3600L);
        
        // Crear source para rutas específicas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Aplicar a todos los endpoints de API
        source.registerCorsConfiguration("/api/**", configuration);
        
        // También para endpoints específicos si es necesario
        source.registerCorsConfiguration("/api/operations/**", configuration);
        
        return source;
    }
}