package com.bpdev.clientservice.controllers;

import com.bpdev.clientservice.dtos.GuestRequest;
import com.bpdev.clientservice.dtos.GuestUpdateRequest;
import com.bpdev.clientservice.entities.Guest;
import com.bpdev.clientservice.services.GuestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody GuestRequest guestRequest) {
        Guest guest = guestService.create(guestRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guest.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Guest> findById(@PathVariable UUID id) {
        Guest guest = guestService.findById(id);
        return ResponseEntity.ok().body(guest);
    }

    @GetMapping(params = "email")
    public ResponseEntity<Guest> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(guestService.findByEmail(email));
    }

    @GetMapping(params = "phoneNumber")
    public ResponseEntity<Guest> findByPhoneNumber(@RequestParam String phoneNumber) {
        return ResponseEntity.ok().body(guestService.findByPhoneNumber(phoneNumber));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updateById
            (@PathVariable UUID id, @RequestBody GuestUpdateRequest guestUpdateRequest) {
        guestService.updateById(id, guestUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        guestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
