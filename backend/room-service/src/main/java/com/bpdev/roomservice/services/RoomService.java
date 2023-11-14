package com.bpdev.roomservice.services;

import com.bpdev.roomservice.exceptions.ResourceNotFoundException;
import com.bpdev.roomservice.exceptions.UniqueConstraintViolationException;
import com.bpdev.roomservice.dtos.RoomRequest;
import com.bpdev.roomservice.dtos.RoomUpdateRequest;
import com.bpdev.roomservice.entities.Room;
import com.bpdev.roomservice.entities.enums.RoomStatus;
import com.bpdev.roomservice.entities.enums.RoomType;
import com.bpdev.roomservice.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomServiceClient roomServiceClient;

    @Autowired
    private MessagingService messagingService;

    public Room create(RoomRequest roomRequest){
        try{
            roomServiceClient.checkIfHotelExists(roomRequest.getHotelId());

            Room room = mapRequestToRoom(roomRequest);

            return roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException("A room with the same reservationId " +
                    "or with the same combination of hotelId and roomNumber already exists.");
        }
    }

    public Room findById(UUID id){
        return roomRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found. ID " + id));
    }

    public List<Room> findAllByHotelId(UUID hotelId){
        return roomRepository.findAllByHotelId(hotelId);
    }

    public List<Room> findAllByHotelIdAndRoomType(UUID hotelId, RoomType roomType){
        return roomRepository.findAllByHotelIdAndRoomType(hotelId, roomType);
    }

    public List<Room> findAllByHotelIdAndRoomStatus(UUID hotelId, RoomStatus roomStatus){
        return roomRepository.findAllByHotelIdAndRoomStatus(hotelId, roomStatus);
    }

    public List<Room> findAllByHotelIdAndBeds(UUID hotelId, int beds){
        return roomRepository.findAllByHotelIdAndBeds(hotelId, beds);
    }

    public List<Room> findAllByHotelIdAndCostPerNightLessThanEqual(UUID hotelId, int costPerNight){
        return roomRepository.findAllByHotelIdAndCostPerNightLessThanEqual(hotelId, costPerNight);
    }

    public void updateById(UUID id, RoomUpdateRequest roomUpdateRequest){
        try{
            Room room = roomRepository.getReferenceById(id);
            updateData(roomUpdateRequest, room);

            roomRepository.save(room);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Room not found. ID " + id);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException("A room with the same reservationId " +
                    "or with the same combination of hotelId and roomNumber already exists.");
        }
    }

    private static void updateData(RoomUpdateRequest roomUpdateRequest, Room room) {
        room.setRoomNumber(roomUpdateRequest.getRoomNumber());
        room.setRoomType(roomUpdateRequest.getRoomType());
        room.setRoomStatus(roomUpdateRequest.getRoomStatus());
        room.setBeds(roomUpdateRequest.getBeds());
        room.setCostPerNight(roomUpdateRequest.getCostPerNight());
        room.setReservationId(roomUpdateRequest.getReservationId());
    }

    public void deleteById(UUID id){
        try {
            roomRepository.deleteById(id);

            messagingService.publishRoomDeletedEvent(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Room not found. ID " + id);
        }
    }

    private static Room mapRequestToRoom(RoomRequest roomRequest) {
        return Room.builder()
                .roomNumber(roomRequest.getRoomNumber())
                .roomType(roomRequest.getRoomType())
                .roomStatus(roomRequest.getRoomStatus())
                .beds(roomRequest.getBeds())
                .costPerNight(roomRequest.getCostPerNight())
                .hotelId(roomRequest.getHotelId())
                .reservationId(roomRequest.getReservationId())
                .build();
    }

    public void deleteAllByHotelId(UUID hotelId) {
        roomRepository.deleteAllByHotelId(hotelId);
    }

    public boolean existsById(UUID id) {
        return roomRepository.existsById(id);
    }
}
