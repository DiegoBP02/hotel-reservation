package com.bpdev.clientservice.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestUpdateRequest {
    @Size(min = 3, max = 50)
    private String name;
    @Size(min = 3, max = 50)
    private String surname;
    @Size(min = 9, max = 15)
    private String phoneNumber;
    @Email
    private String email;
    private boolean isChild;
}
