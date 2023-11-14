package com.bpdev.clientservice.repositories;

import com.bpdev.clientservice.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {
    Optional<Guest> findByEmail(String email);
    Optional<Guest> findByPhoneNumber(String phoneNumber);
    boolean existsAllByIdIn(List<UUID> ids);
}
