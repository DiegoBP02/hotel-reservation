package com.bpdev.hotelservice.controllers;

import com.bpdev.hotelservice.dtos.HotelRequest;
import com.bpdev.hotelservice.dtos.HotelUpdateRequest;
import com.bpdev.hotelservice.entities.Hotel;
import com.bpdev.hotelservice.services.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody HotelRequest hotelRequest) {
        Hotel hotel = hotelService.create(hotelRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(hotel.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> findAll() {
        return ResponseEntity.ok().body(hotelService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Hotel> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(hotelService.findById(id));
    }

    @GetMapping(params = "stars")
    public ResponseEntity<List<Hotel>> findAllByStars(@RequestParam int stars) {
        return ResponseEntity.ok().body(hotelService.findAllByStars(stars));
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<Hotel>> findAllByName(@RequestParam String name) {
        return ResponseEntity.ok().body(hotelService.findAllByName(name));
    }

    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        boolean exists = hotelService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updateById
            (@PathVariable UUID id,
             @Valid @RequestBody HotelUpdateRequest hotelUpdateRequest) {
        hotelService.updateById(id, hotelUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
