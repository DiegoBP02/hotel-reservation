package com.dbdev.roomservice.entities;

import com.dbdev.roomservice.entities.enums.RoomStatus;
import com.dbdev.roomservice.entities.enums.RoomType;
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
@Table(name = "t_room", uniqueConstraints = @UniqueConstraint(columnNames = {"roomNumber", "hotelId"}))
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Size(min = 5, max = 5)
    @Column(nullable = false, length = 5)
    private String roomNumber;
    @NotNull
    @Column(nullable = false)
    private RoomType roomType;
    @NotNull
    @Column(nullable = false)
    private RoomStatus roomStatus;
    @NotNull
    @Min(0)
    @Max(10)
    @Column(nullable = false)
    private int beds;
    @Positive
    @Column(nullable = false)
    private int costPerNight;
    @NotNull
    @Column(nullable = false)
    private UUID hotelId;
    @Column(unique = true)
    private UUID reservationId;
}

