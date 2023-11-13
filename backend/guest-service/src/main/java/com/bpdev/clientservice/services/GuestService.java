package com.bpdev.clientservice.services;

import com.bpdev.clientservice.dtos.GuestRequest;
import com.bpdev.clientservice.dtos.GuestUpdateRequest;
import com.bpdev.clientservice.entities.Guest;
import com.bpdev.clientservice.exceptions.ResourceNotFoundException;
import com.bpdev.clientservice.exceptions.UniqueConstraintViolationException;
import com.bpdev.clientservice.repositories.GuestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    public Guest create(GuestRequest guestRequest){
        try{
            Guest guest = mapRequestToGuest(guestRequest);

            return guestRepository.save(guest);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException
                    ("A guest with the same email or phone number already exists.");
        }
    }

    public Guest findById(UUID id){
        return guestRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Guest not found. ID " + id));
    }

    public Guest findByEmail(String email){
        return guestRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Guest not found. Email " + email));
    }

    public Guest findByPhoneNumber(String phoneNumber){
        return guestRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new
                        ResourceNotFoundException("Guest not found. Phone number " + phoneNumber));
    }


    public void updateById(UUID id, GuestUpdateRequest guestUpdateRequest){
        try{
            Guest guest = guestRepository.getReferenceById(id);
            updateData(guestUpdateRequest, guest);

            guestRepository.save(guest);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Guest not found. ID " + id);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException
                    ("A guest with the same email or phone number already exists.");
        }
    }

    private static void updateData(GuestUpdateRequest guestUpdateRequest, Guest guest) {
        guest.setName(guestUpdateRequest.getName());
        guest.setSurname(guestUpdateRequest.getSurname());
        guest.setEmail(guestUpdateRequest.getEmail());
        guest.setPhoneNumber(guestUpdateRequest.getPhoneNumber());
        guest.setChild(guestUpdateRequest.isChild());
    }

    public void deleteById(UUID id){
        try {
            guestRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Guest not found. ID " + id);
        }
    }

    private static Guest mapRequestToGuest(GuestRequest guestRequest) {
        return Guest.builder()
                .name(guestRequest.getName())
                .surname(guestRequest.getSurname())
                .email(guestRequest.getEmail())
                .phoneNumber(guestRequest.getPhoneNumber())
                .isChild(guestRequest.isChild())
                .build();
    }

}
