package com.bpdev.reservationservice.repositories;

import com.bpdev.reservationservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByPaymentId(UUID id);
    List<Reservation> findAllByRoomId(UUID id);
    List<Reservation> findAllByGuestsIdsIn(List<UUID> guestsIds);
}
