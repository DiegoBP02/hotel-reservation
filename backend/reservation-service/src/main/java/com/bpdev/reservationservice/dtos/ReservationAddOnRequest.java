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
public class ReservationAddOnRequest {
    @Positive
    @Column(nullable = false)
    private Integer costPerNight;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String name;
}
