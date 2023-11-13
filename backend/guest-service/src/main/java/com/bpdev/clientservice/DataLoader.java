package com.bpdev.clientservice;

import com.bpdev.clientservice.entities.Guest;
import com.bpdev.clientservice.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public void run(String... args) throws Exception {
        createGuest();
    }

    private void createGuest() {
        Guest guest = Guest.builder()
                .id(UUID.randomUUID())
                .name("John")
                .surname("Doe")
                .phoneNumber("123456789")
                .email("john.doe@example.com")
                .isChild(false)
                .build();
        Guest savedGuest = guestRepository.save(guest);
        System.out.println("Guest saved: " + savedGuest.getId());
    }
}