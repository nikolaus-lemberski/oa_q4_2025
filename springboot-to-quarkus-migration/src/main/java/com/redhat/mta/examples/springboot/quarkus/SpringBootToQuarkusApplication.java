package com.redhat.mta.examples.springboot.quarkus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application Main Class
 * 
 * Deprecated patterns for Quarkus migration:
 * - @SpringBootApplication annotation will be removed in Quarkus
 * - SpringApplication.run() method will be replaced by Quarkus runtime
 * - Configuration annotations will be replaced with Quarkus equivalents
 * 
 * This class demonstrates the main entry point pattern that needs migration.
 */
@SpringBootApplication
public class SpringBootToQuarkusApplication {

    /**
     * Main method - entry point for Spring Boot application
     * In Quarkus, this entire class and method will be removed
     * as Quarkus handles application bootstrap automatically
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootToQuarkusApplication.class, args);
    }
}

