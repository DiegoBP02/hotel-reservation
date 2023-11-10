package com.bpdev.hotelservice.repositories;

import com.bpdev.hotelservice.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    List<Hotel> findAllByStars(int stars);
    List<Hotel> findAllByName(String name);
}
