package com.bpdev.reservationservice.controllers;

import com.bpdev.reservationservice.dtos.ReservationAddOnRequest;
import com.bpdev.reservationservice.dtos.ReservationAddOnUpdateRequest;
import com.bpdev.reservationservice.entities.ReservationAddOn;
import com.bpdev.reservationservice.services.ReservationAddOnService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservationAddOn")
public class ReservationAddOnController {

    @Autowired
    private ReservationAddOnService reservationAddOnService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ReservationAddOnRequest reservationAddOnRequest) {
        ReservationAddOn reservationAddOn = reservationAddOnService.create(reservationAddOnRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationAddOn.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationAddOn>> findAll() {
        return ResponseEntity.ok().body(reservationAddOnService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReservationAddOn> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(reservationAddOnService.findById(id));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updateById
            (@PathVariable UUID id,
             @Valid @RequestBody ReservationAddOnUpdateRequest reservationAddOnUpdateRequest) {
        reservationAddOnService.updateById(id, reservationAddOnUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reservationAddOnService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
