package com.quantumdev.integraservicios.operationManagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.quantumdev.integraservicios.operationManagement.Jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    // Constantes para los roles que vienen del JWT
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    
    /** El filtro de autenticación JWT que se aplicará a las solicitudes entrantes */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /** La fuente de configuración CORS para definir orígenes y métodos permitidos */
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                
                // Deshabilitar CSRF para APIs REST
                .csrf(csrf -> csrf.disable())
                
                // Configurar gestión de sesiones como stateless para JWT
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Configurar reglas de autorización
                .authorizeHttpRequests(auth -> auth
                    // Endpoints públicos - documentación de API
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    
                    // Endpoints de hardware - permisos mixtos
                    .requestMatchers(HttpMethod.GET, "/api/operations/hardware").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/hardware/availability").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/hardware/user/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/hardware/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/operations/hardware").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/operations/hardware/*").permitAll()
                    // Solo ROLE_ADMIN puede hacer handOver y return
                    .requestMatchers(HttpMethod.POST, "/api/operations/hardware/*/handOver").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/operations/hardware/*/return").permitAll()
                    
                    // Endpoints de espacios - permisos mixtos  
                    .requestMatchers(HttpMethod.GET, "/api/operations/space").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/space/availability").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/space/user/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/operations/space/*").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/operations/space").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/operations/space/*").permitAll()
                    // Solo ROLE_ADMIN puede hacer handOver y return
                    .requestMatchers(HttpMethod.POST, "/api/operations/space/*/handOver").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/operations/space/*/return").permitAll()
                    
                    // Todas las demás solicitudes requieren autenticación
                    .anyRequest().authenticated()
                )
                
                // Agregar filtro JWT antes de la autenticación por usuario/contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                
                // Manejo de excepciones
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(401);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"No autorizado\",\"message\":\"Se requiere autenticación\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(403);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Prohibido\",\"message\":\"Privilegios insuficientes\"}");
                    })
                )
                
                .build();
    }
}