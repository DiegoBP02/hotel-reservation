package com.dbdev.roomservice.repositories;

import com.dbdev.roomservice.entities.Room;
import com.dbdev.roomservice.entities.enums.RoomStatus;
import com.dbdev.roomservice.entities.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findAllByHotelId(UUID hotelId);
    List<Room> findAllByHotelIdAndRoomType(UUID hotelId, RoomType roomType);
    List<Room> findAllByHotelIdAndRoomStatus(UUID hotelId, RoomStatus roomStatus);
    List<Room> findAllByHotelIdAndBeds(UUID hotelId, int beds);
    List<Room> findAllByHotelIdAndCostPerNightLessThanEqual(UUID hotelId, int costPerNight);
}
