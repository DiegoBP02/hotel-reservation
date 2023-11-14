package com.bpdev.reservationservice.entities;

import com.bpdev.reservationservice.entities.embedabbles.ReservationDate;
import com.bpdev.reservationservice.entities.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "t_reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(nullable = false)
    private ReservationStatus reservationStatus;
    @Column(nullable = false)
    private ReservationDate reservationDate;
    @NotNull
    @Column(nullable = false)
    private List<UUID> guestsIds;
    private UUID paymentId;
    @NotNull
    @Column(nullable = false)
    private UUID roomId;
    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer amount;
    @ManyToMany
    private List<ReservationAddOn> reservationAddOns;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdTime;
}

