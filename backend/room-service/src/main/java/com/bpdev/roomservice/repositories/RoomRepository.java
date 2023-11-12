package com.bpdev.roomservice.repositories;

import com.bpdev.roomservice.entities.Room;
import com.bpdev.roomservice.entities.enums.RoomStatus;
import com.bpdev.roomservice.entities.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findAllByHotelId(UUID hotelId);
    List<Room> findAllByHotelIdAndRoomType(UUID hotelId, RoomType roomType);
    List<Room> findAllByHotelIdAndRoomStatus(UUID hotelId, RoomStatus roomStatus);
    List<Room> findAllByHotelIdAndBeds(UUID hotelId, int beds);
    List<Room> findAllByHotelIdAndCostPerNightLessThanEqual(UUID hotelId, int costPerNight);

    void deleteAllByHotelId(UUID hotelId);
}
