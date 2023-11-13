package com.bpdev.reservationservice.dtos;

import com.bpdev.reservationservice.entities.ReservationAddOn;
import com.bpdev.reservationservice.entities.embedabbles.ReservationDate;
import com.bpdev.reservationservice.entities.enums.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateRequest {
    @NotNull
    private ReservationStatus reservationStatus;
    @NotNull
    private ReservationDate reservationDate;
    @NotNull
    private List<UUID> guestsIds;
    @NotNull
    private UUID paymentId;
    @NotNull
    @PositiveOrZero
    private Integer amount;
    private List<ReservationAddOn> reservationAddOns;
}