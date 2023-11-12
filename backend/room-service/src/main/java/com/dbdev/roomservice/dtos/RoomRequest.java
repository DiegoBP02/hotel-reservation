package com.dbdev.roomservice.dtos;

import com.dbdev.roomservice.entities.enums.RoomStatus;
import com.dbdev.roomservice.entities.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotBlank
    @Size(min = 5, max = 5)
    private String roomNumber;
    @NotNull
    private RoomType roomType;
    @NotNull
    private RoomStatus roomStatus;
    @NotNull
    @Min(0)
    @Max(10)
    private int beds;
    @Positive
    private int costPerNight;
    @NotNull
    private UUID hotelId;
    private UUID reservationId;
}
