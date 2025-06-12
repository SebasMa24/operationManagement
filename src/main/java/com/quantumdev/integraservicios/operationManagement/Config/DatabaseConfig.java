package com.quantumdev.integraservicios.operationManagement.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.quantumdev.integraservicios.operationManagement.entity")
@EnableJpaRepositories("com.quantumdev.integraservicios.operationManagement.repository")
public class DatabaseConfig {
}