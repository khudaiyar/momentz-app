package com.momentz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
        scanBasePackages = "com.momentz",
        exclude = {
                org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration.class
        }
)
@EnableJpaAuditing
public class MomentzApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentzApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("   MOMENTZ APPLICATION STARTED!");
        System.out.println("   Access at: http://localhost:8081");
        System.out.println("   Connected to PostgreSQL Database");
        System.out.println("===========================================\n");
    }
}