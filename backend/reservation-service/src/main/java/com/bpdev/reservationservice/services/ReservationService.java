package com.bpdev.reservationservice.services;

import com.bpdev.reservationservice.dtos.ReservationRequest;
import com.bpdev.reservationservice.dtos.ReservationUpdateRequest;
import com.bpdev.reservationservice.entities.Reservation;
import com.bpdev.reservationservice.entities.ReservationAddOn;
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

    @Autowired
    private ReservationServiceClient reservationServiceClient;

    @Autowired
    private ReservationAddOnService reservationAddOnService;

    public Reservation create(ReservationRequest reservationRequest) {
        reservationServiceClient.checkIfRoomExists(reservationRequest.getRoomId());
        reservationServiceClient.checkIfGuestsExists(reservationRequest.getGuestsIds());

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

    private void updateData(ReservationUpdateRequest reservationUpdateRequest,
                                   Reservation reservation) {
        if(reservationUpdateRequest.getPaymentId() != null){
            reservationServiceClient.checkIfGuestsExists(reservationUpdateRequest.getGuestsIds());
            reservation.setGuestsIds(reservationUpdateRequest.getGuestsIds());
        }
        reservation.setReservationStatus(reservationUpdateRequest.getReservationStatus());
        reservation.setReservationDate(reservationUpdateRequest.getReservationDate());
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

    private Reservation mapRequestToReservation(ReservationRequest reservationRequest) {
        List<ReservationAddOn> reservationAddOns
                = reservationAddOnService.findAllByIdIn(reservationRequest.getReservationAddOnsIds());

        return Reservation.builder()
                .reservationStatus(reservationRequest.getReservationStatus())
                .reservationDate(reservationRequest.getReservationDate())
                .guestsIds(reservationRequest.getGuestsIds())
                .roomId(reservationRequest.getRoomId())
                .amount(reservationRequest.getAmount())
                .createdTime(LocalDateTime.now())
                .build();
    }

    public boolean existsById(UUID id) {
        return reservationRepository.existsById(id);
    }

    public void deleteAllByRoomId(UUID roomId) {
        reservationRepository.deleteAllByRoomId(roomId);
    }
}
