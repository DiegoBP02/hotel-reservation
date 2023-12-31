package com.bpdev.reservationservice.repositories;

import com.bpdev.reservationservice.entities.ReservationAddOn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationAddOnRepository extends JpaRepository<ReservationAddOn, UUID> {
    List<ReservationAddOn> findAllByIdIn(List<UUID> reservationAddOnsIds);
}
