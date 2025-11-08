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

        // Print environment variables for debugging
        String dbUrl = System.getenv("DATABASE_URL");
        String dbUser = System.getenv("DB_USERNAME");
        String dbPass = System.getenv("DB_PASSWORD");

        System.out.println("\n===========================================");
        System.out.println("ENV VARIABLES AT STARTUP:");
        System.out.println("DATABASE_URL=" + dbUrl);
        System.out.println("DB_USERNAME=" + dbUser);
        System.out.println("DB_PASSWORD=" + (dbPass != null ? "********" : null));
        System.out.println("===========================================\n");

        // Fail fast if any variable is missing
        if (dbUrl == null || dbUser == null || dbPass == null) {
            System.err.println("ERROR: PostgreSQL environment variables are missing! Application will exit.");
            System.exit(1);
        }

        SpringApplication.run(MomentzApplication.class, args);

        System.out.println("\n===========================================");
        System.out.println("   MOMENTZ APPLICATION STARTED!");
        System.out.println("   Access at: http://localhost:8081");
        System.out.println("   Connected to PostgreSQL Database");
        System.out.println("===========================================\n");
    }
}
