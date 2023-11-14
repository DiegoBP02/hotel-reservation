package com.bpdev.roomservice.dtos;

import com.bpdev.roomservice.entities.enums.RoomStatus;
import com.bpdev.roomservice.entities.enums.RoomType;
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
public class RoomUpdateRequest {
    @Size(min = 5, max = 5)
    private String roomNumber;
    private RoomType roomType;
    private RoomStatus roomStatus;
    @Min(0)
    @Max(10)
    private int beds;
    @Positive
    private int costPerNight;
    private UUID reservationId;
}
