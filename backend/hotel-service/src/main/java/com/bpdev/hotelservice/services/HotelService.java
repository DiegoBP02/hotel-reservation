package com.bpdev.hotelservice.services;

import com.bpdev.hotelservice.dtos.HotelRequest;
import com.bpdev.hotelservice.dtos.HotelUpdateRequest;
import com.bpdev.hotelservice.entities.Hotel;
import com.bpdev.hotelservice.exceptions.ResourceNotFoundException;
import com.bpdev.hotelservice.exceptions.UniqueConstraintViolationException;
import com.bpdev.hotelservice.repositories.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel create(HotelRequest hotelRequest){
        try{
            Hotel hotel = mapRequestToHotel(hotelRequest);

            return hotelRepository.save(hotel);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException("A hotel with the same name already exists.");
        }
    }

    public List<Hotel> findAll(){
        return hotelRepository.findAll();
    }

    public Hotel findById(UUID id){
        return hotelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found. ID " + id));
    }

    public List<Hotel> findAllByStars(int stars){
        return hotelRepository.findAllByStars(stars);
    }

    public List<Hotel> findAllByName(String name){
        return hotelRepository.findAllByName(name);
    }

    public void updateById(UUID id, HotelUpdateRequest hotelUpdateRequest){
        try{
            Hotel hotel = hotelRepository.getReferenceById(id);
            updateData(hotelUpdateRequest, hotel);

            hotelRepository.save(hotel);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Hotel not found. ID " + id);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException("A hotel with the same name already exists.");
        }
    }

    private static void updateData(HotelUpdateRequest hotelUpdateRequest, Hotel hotel) {
        hotel.setName(hotelUpdateRequest.getName());
        hotel.setAddress(hotelUpdateRequest.getAddress());
        hotel.setStars(hotelUpdateRequest.getStars());
        hotel.setEmail(hotelUpdateRequest.getEmail());
        hotel.setStandardCheckoutTime(hotelUpdateRequest.getStandardCheckoutTime());
        hotel.setAmenities(hotelUpdateRequest.getAmenities());
        hotel.setLateCheckoutFee(hotelUpdateRequest.getLateCheckoutFee());
    }

    public void deleteById(UUID id){
        try {
            hotelRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Hotel not found. ID " + id);
        }
    }

    private static Hotel mapRequestToHotel(HotelRequest hotelRequest) {
        return Hotel.builder()
                .name(hotelRequest.getName())
                .address(hotelRequest.getAddress())
                .stars(hotelRequest.getStars())
                .email(hotelRequest.getEmail())
                .standardCheckoutTime(hotelRequest.getStandardCheckoutTime())
                .amenities(hotelRequest.getAmenities())
                .lateCheckoutFee(hotelRequest.getLateCheckoutFee())
                .build();
    }

}
