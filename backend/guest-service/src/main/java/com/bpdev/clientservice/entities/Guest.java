package com.bpdev.clientservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "t_guest")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String name;
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String surname;
    @NotBlank
    @Size(min = 9, max = 15)
    @Column(nullable = false, length = 15, unique = true)
    private String phoneNumber;
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull
    @Column(nullable = false)
    private boolean isChild;
}

