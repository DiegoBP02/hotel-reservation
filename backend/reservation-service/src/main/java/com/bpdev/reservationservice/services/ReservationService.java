package com.bpdev.reservationservice.services;

import com.bpdev.reservationservice.dtos.ReservationRequest;
import com.bpdev.reservationservice.dtos.ReservationUpdateRequest;
import com.bpdev.reservationservice.entities.Reservation;
import com.bpdev.reservationservice.exceptions.ResourceNotFoundException;
import com.bpdev.reservationservice.exceptions.UniqueConstraintViolationException;
import com.bpdev.reservationservice.repositories.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation create(ReservationRequest reservationRequest) {
        Reservation reservation = mapRequestToReservation(reservationRequest);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found. ID " + id));
    }

    public Reservation findByPaymentId(UUID id) {
        return reservationRepository.findByPaymentId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found. ID " + id));
    }

    public List<Reservation> findAllByRoomId(UUID id) {
        return reservationRepository.findAllByRoomId(id);
    }

    public List<Reservation> findAllByGuestsIdsIn(List<UUID> ids) {
        return reservationRepository.findAllByGuestsIdsIn(ids);
    }

    public void updateById(UUID id, ReservationUpdateRequest reservationUpdateRequest) {
        try {
            Reservation reservation = reservationRepository.getReferenceById(id);
            updateData(reservationUpdateRequest, reservation);

            reservationRepository.save(reservation);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Reservation not found. ID " + id);
        }
    }

    private static void updateData(ReservationUpdateRequest reservationUpdateRequest,
                                   Reservation reservation) {
        reservation.setReservationStatus(reservationUpdateRequest.getReservationStatus());
        reservation.setReservationDate(reservationUpdateRequest.getReservationDate());
        reservation.setGuestsIds(reservationUpdateRequest.getGuestsIds());
        reservation.setPaymentId(reservationUpdateRequest.getPaymentId());
        reservation.setAmount(reservationUpdateRequest.getAmount());
    }

    public void deleteById(UUID id) {
        try {
            reservationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Reservation not found. ID " + id);
        }
    }

    private static Reservation mapRequestToReservation(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .reservationStatus(reservationRequest.getReservationStatus())
                .reservationDate(reservationRequest.getReservationDate())
                .guestsIds(reservationRequest.getGuestsIds())
                .paymentId(reservationRequest.getPaymentId())
                .roomId(reservationRequest.getRoomId())
                .amount(reservationRequest.getAmount())
                .createdTime(LocalDateTime.now())
                .build();
    }

    public boolean existsById(UUID id) {
        return reservationRepository.existsById(id);
    }
}
