package com.bpdev.reservationservice.entities.embedabbles;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ReservationDate {
    @NotNull
    @Column(nullable = false)
    private LocalDateTime checkInDate;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime checkOutDate;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime estimatedCheckOutTime;
    @NotNull
    @Column(nullable = false)
    private Boolean lateCheckout;
}
