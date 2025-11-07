package com.momentz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.momentz")
@EnableJpaAuditing
public class MomentzApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentzApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("   MOMENTZ APPLICATION STARTED!");
        System.out.println("   Access at: http://localhost:8081");
        System.out.println("   H2 Console: http://localhost:8081/h2-console");
        System.out.println("===========================================\n");
    }
}