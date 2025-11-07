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

        // Log database info (mask password)
        String dbUrl = System.getenv("DATABASE_URL");
        String dbUser = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        System.out.println("   DATABASE_URL: " + (dbUrl != null ? dbUrl : "NULL"));
        System.out.println("   DB_USERNAME: " + (dbUser != null ? dbUser : "NULL"));
        System.out.println("   DB_PASSWORD: " + (dbPassword != null ? "********" : "NULL"));

        // H2 fallback info
        if (dbUrl == null || dbUrl.isEmpty()) {
            System.out.println("   ⚠️ DATABASE_URL not set. Using H2 in-memory database!");
            System.out.println("   H2 Console: http://localhost:8081/h2-console");
        } else {
            System.out.println("   ✅ Using Supabase/Postgres database.");
        }

        System.out.println("===========================================\n");
    }
}
