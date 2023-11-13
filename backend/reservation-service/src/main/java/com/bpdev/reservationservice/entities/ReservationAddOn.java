package com.bpdev.reservationservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "t_reservation_add_on")
public class ReservationAddOn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Positive
    @Column(nullable = false)
    private Integer costPerNight;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String name;
}
