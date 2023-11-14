package com.bpdev.roomservice.controllers;

import com.bpdev.roomservice.entities.Room;
import com.bpdev.roomservice.entities.enums.RoomStatus;
import com.bpdev.roomservice.entities.enums.RoomType;
import com.bpdev.roomservice.services.RoomService;
import com.bpdev.roomservice.dtos.RoomRequest;
import com.bpdev.roomservice.dtos.RoomUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody RoomRequest roomRequest) {
        Room room = roomService.create(roomRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(room.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Room> findById(@PathVariable UUID id) {
        Room room = roomService.findById(id);
        return ResponseEntity.ok().body(room);
    }

    @GetMapping
    public ResponseEntity<List<Room>> findAllByHotelId(@RequestParam UUID hotelId) {
        return ResponseEntity.ok().body(roomService.findAllByHotelId(hotelId));
    }

    @GetMapping(params = {"hotelId", "roomType"})
    public ResponseEntity<List<Room>> findAllByHotelIdAndRoomType(
            @RequestParam UUID hotelId,
            @RequestParam RoomType roomType
    ) {
        List<Room> rooms = roomService.findAllByHotelIdAndRoomType(hotelId, roomType);
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping(params = {"hotelId", "roomStatus"})
    public ResponseEntity<List<Room>> findAllByHotelIdAndRoomStatus(
            @RequestParam UUID hotelId,
            @RequestParam RoomStatus roomStatus
    ) {
        List<Room> rooms = roomService.findAllByHotelIdAndRoomStatus(hotelId, roomStatus);
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping(params = {"hotelId", "beds"})
    public ResponseEntity<List<Room>> findAllByHotelIdAndBeds(
            @RequestParam UUID hotelId,
            @RequestParam int beds
    ) {
        List<Room> rooms = roomService.findAllByHotelIdAndBeds(hotelId, beds);
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping(params = {"hotelId", "costPerNight"})
    public ResponseEntity<List<Room>> findAllByHotelIdAndCostPerNightLessThanEqual(
            @RequestParam UUID hotelId,
            @RequestParam int costPerNight
    ) {
        List<Room> rooms = roomService.findAllByHotelIdAndCostPerNightLessThanEqual(hotelId, costPerNight);
        return ResponseEntity.ok().body(rooms);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updateById
            (@PathVariable UUID id, @Valid @RequestBody RoomUpdateRequest hotelUpdateRequest) {
        roomService.updateById(id, hotelUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        boolean exists = roomService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
