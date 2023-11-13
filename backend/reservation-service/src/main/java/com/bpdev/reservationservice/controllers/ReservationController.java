package com.bpdev.reservationservice.controllers;

import com.bpdev.reservationservice.dtos.ReservationRequest;
import com.bpdev.reservationservice.dtos.ReservationUpdateRequest;
import com.bpdev.reservationservice.entities.Reservation;
import com.bpdev.reservationservice.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.create(reservationRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservation.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> findAll() {
        return ResponseEntity.ok().body(reservationService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(reservationService.findById(id));
    }

    @GetMapping(params = "paymentId")
    public ResponseEntity<Reservation> findByPaymentId(@RequestParam UUID paymentId) {
        return ResponseEntity.ok().body(reservationService.findByPaymentId(paymentId));
    }

    @GetMapping(params = "roomId")
    public ResponseEntity<List<Reservation>> findAllByRoomId(@RequestParam UUID roomId) {
        return ResponseEntity.ok().body(reservationService.findAllByRoomId(roomId));
    }

    @GetMapping(params = "guestId")
    public ResponseEntity<List<Reservation>> findAllByGuestsIds(@RequestParam List<UUID> guestsIds) {
        return ResponseEntity.ok().body(reservationService.findAllByGuestsIdsIn(guestsIds));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updateById
            (@PathVariable UUID id, @RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        reservationService.updateById(id, reservationUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
