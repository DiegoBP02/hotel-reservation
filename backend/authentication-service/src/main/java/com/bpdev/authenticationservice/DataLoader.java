package com.bpdev.authenticationservice;

import com.bpdev.authenticationservice.entities.User;
import com.bpdev.authenticationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdmin();
    }

    private void createAdmin() {
        User admin = User.builder()
                .name("admin")
                .password(passwordEncoder.encode("senha"))
                .build();
        User savedAdmin = userRepository.save(admin);
        System.out.println("Admin saved: " + savedAdmin.getId());
    }
}