package com.bpdev.reservationservice.services;

import com.bpdev.reservationservice.dtos.ReservationAddOnRequest;
import com.bpdev.reservationservice.dtos.ReservationAddOnUpdateRequest;
import com.bpdev.reservationservice.entities.ReservationAddOn;
import com.bpdev.reservationservice.exceptions.ResourceNotFoundException;
import com.bpdev.reservationservice.exceptions.UniqueConstraintViolationException;
import com.bpdev.reservationservice.repositories.ReservationAddOnRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationAddOnService {

    @Autowired
    private ReservationAddOnRepository reservationAddOnRepository;

    public ReservationAddOn create(ReservationAddOnRequest reservationAddOnRequest){
        try{
            ReservationAddOn reservationAddOn = mapRequestToReservationAddOn(reservationAddOnRequest);

            return reservationAddOnRepository.save(reservationAddOn);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException
                    ("A reservationAddOn with the same name already exists.");
        }
    }

    public List<ReservationAddOn> findAll(){
        return reservationAddOnRepository.findAll();
    }

    public ReservationAddOn findById(UUID id){
        return reservationAddOnRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException
                        ("ReservationAddOn not found. ID " + id));
    }

    public void updateById(UUID id, ReservationAddOnUpdateRequest reservationAddOnUpdateRequest){
        try{
            ReservationAddOn reservationAddOn = reservationAddOnRepository.getReferenceById(id);
            updateData(reservationAddOnUpdateRequest, reservationAddOn);

            reservationAddOnRepository.save(reservationAddOn);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ReservationAddOn not found. ID " + id);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException
                    ("A reservationAddOn with the same name already exists.");
        }
    }

    private static void updateData(ReservationAddOnUpdateRequest reservationAddOnUpdateRequest,
                                   ReservationAddOn reservationAddOn) {
        reservationAddOn.setName(reservationAddOnUpdateRequest.getName());
        reservationAddOn.setCostPerNight(reservationAddOnUpdateRequest.getCostPerNight());
    }

    public void deleteById(UUID id){
        try {
            reservationAddOnRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ReservationAddOn not found. ID " + id);
        }
    }
    private static ReservationAddOn mapRequestToReservationAddOn
            (ReservationAddOnRequest reservationAddOnRequest) {
        return ReservationAddOn.builder()
                .name(reservationAddOnRequest.getName())
                .costPerNight(reservationAddOnRequest.getCostPerNight())
                .build();
    }

    public List<ReservationAddOn> findAllByIdIn(List<UUID> reservationAddOnsIds) {
        return reservationAddOnRepository.findAllByIdIn(reservationAddOnsIds);
    }
}
