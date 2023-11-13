package com.bpdev.reservationservice.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAddOnUpdateRequest {
    @Positive
    @Column(nullable = false)
    private Integer costPerNight;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String name;
}
