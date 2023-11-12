package com.bpdev.hotelservice.entities;

import com.bpdev.hotelservice.entities.embedabbles.Address;
import com.bpdev.hotelservice.entities.enums.Amenities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "t_hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Column(nullable = false, length = 50, unique = true)
    private String name;
    @NotNull
    @Column(nullable = false)
    private Address address;
    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private int stars;
    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;
    private List<Amenities> amenities;
    @NotNull
    @Column(nullable = false)
    private LocalTime standardCheckoutTime;
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer lateCheckoutFee;
}

