package com.dbdev.roomservice;

import com.dbdev.roomservice.entities.Room;
import com.dbdev.roomservice.entities.enums.RoomStatus;
import com.dbdev.roomservice.entities.enums.RoomType;
import com.dbdev.roomservice.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        createRoom();
    }

    private void createRoom() {
        Room room = Room.builder()
                .roomNumber("12345")
                .roomType(RoomType.Economy)
                .roomStatus(RoomStatus.Available)
                .beds(2)
                .costPerNight(100)
                .hotelId(UUID.randomUUID())
                .build();
        Room savedRoom = roomRepository.save(room);
        System.out.println("Room saved: " + savedRoom.getId());
    }
}