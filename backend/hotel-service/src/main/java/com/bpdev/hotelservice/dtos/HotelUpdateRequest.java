package com.bpdev.hotelservice.dtos;

import com.bpdev.hotelservice.entities.embedabbles.Address;
import com.bpdev.hotelservice.entities.enums.Amenities;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelUpdateRequest {
    @Size(min = 3, max = 50)
    private String name;
    private Address address;
    @Min(0)
    @Max(5)
    private int stars;
    @Email
    @Column(nullable = false)
    private String email;
    private List<UUID> roomsIds;
    private List<Amenities> amenities;
    private LocalTime standardCheckoutTime;
    @Positive
    private Integer lateCheckoutFee;
}