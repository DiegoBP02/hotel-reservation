package com.bpdev.hotelservice.entities.embedabbles;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {
    @NotBlank
    @Column(nullable = false, length = 100)
    private String street;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String city;
    @NotBlank
    @Column(nullable = false, length = 50)
    private String state;
    @NotBlank
    @Column(nullable = false, length = 20)
    private String zipCode;
}
