package com.momentz.config;

import com.momentz.model.Role;
import com.momentz.model.Role.ERole;
import com.momentz.model.User;
import com.momentz.repository.RoleRepository;
import com.momentz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);

            Role modRole = new Role();
            modRole.setName(ERole.ROLE_MODERATOR);
            roleRepository.save(modRole);

            System.out.println("✓ Roles initialized");
        }

        // Create or update admin user
        User admin = userRepository.findByUsername("admin").orElse(null);

        if (admin == null) {
            // Admin doesn't exist, create it
            admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@momentz.com");
            admin.setFullName("Administrator");
            admin.setBio("Momentz Platform Administrator");
            admin.setIsVerified(true);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role not found")));
            roles.add(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found")));
            admin.setRoles(roles);

            System.out.println("✓ Admin user created");
        } else {
            System.out.println("✓ Admin user already exists, updating password");
        }

        // Always update password
        admin.setPassword(passwordEncoder.encode("NewStrongPassword123!"));
        userRepository.save(admin);

        System.out.println("  Username: " + admin.getUsername());
        System.out.println("  Password: NewStrongPassword123!");
        System.out.println("\n===========================================");
        System.out.println("   DATABASE INITIALIZED SUCCESSFULLY!");
        System.out.println("===========================================\n");
    }
}
