package com.bpdev.reservationservice;

import com.bpdev.reservationservice.entities.Reservation;
import com.bpdev.reservationservice.entities.ReservationAddOn;
import com.bpdev.reservationservice.entities.embedabbles.ReservationDate;
import com.bpdev.reservationservice.entities.enums.ReservationStatus;
import com.bpdev.reservationservice.repositories.ReservationAddOnRepository;
import com.bpdev.reservationservice.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationAddOnRepository reservationAddOnRepository;

    @Override
    public void run(String... args) throws Exception {
        createReservation();
    }

    private void createReservation() {
        Reservation reservation = Reservation.builder()
                .reservationStatus(ReservationStatus.CONFIRMED)
                .reservationDate(createReservationDate())
                .guestsIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()))
                .paymentId(UUID.randomUUID())
                .roomId(UUID.randomUUID())
                .amount(100)
                .reservationAddOns(List.of(createReservationAddOn()))
                .createdTime(LocalDateTime.now())
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);
        System.out.println("Reservation saved: " + savedReservation.getId());
    }

    private ReservationDate createReservationDate() {
        return ReservationDate.builder()
                .checkInDate(LocalDateTime.now().plusDays(1))
                .checkOutDate(LocalDateTime.now().plusDays(5))
                .estimatedCheckOutTime(LocalDateTime.now().plusDays(5).plusHours(2))
                .lateCheckout(false)
                .build();
    }

    private ReservationAddOn createReservationAddOn() {
        ReservationAddOn reservationAddOn = ReservationAddOn.builder()
                .costPerNight(20)
                .name("WiFi Access")
                .build();
        ReservationAddOn savedReservationAddOn = reservationAddOnRepository.save(reservationAddOn);
        System.out.println("ReservationAddOn saved: " + savedReservationAddOn.getId());
        return savedReservationAddOn;
    }
}