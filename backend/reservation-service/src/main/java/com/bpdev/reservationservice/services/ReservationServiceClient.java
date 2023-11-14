package com.bpdev.reservationservice.services;

import com.bpdev.reservationservice.exceptions.GuestNotFoundException;
import com.bpdev.reservationservice.exceptions.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationServiceClient {

    private final String ROOM_SERVICE_URL = "http://ROOM-SERVICE/api/v1/room/";
    private final String GUEST_SERVICE_URL = "http://GUEST-SERVICE/api/v1/guest";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void checkIfRoomExists(UUID roomId) {
        String url = UriComponentsBuilder
                .fromHttpUrl(ROOM_SERVICE_URL + roomId + "/exists")
                .toUriString();

        boolean exists = Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        if (!exists) {
            throw new RoomNotFoundException("Room not found. ID: " + roomId);
        }
    }

    public void checkIfGuestsExists(List<UUID> guestsIds) {
        String url = UriComponentsBuilder
                .fromHttpUrl(GUEST_SERVICE_URL)
                .queryParam("exists", guestsIds.stream().map(UUID::toString).toList())
                .toUriString();

        boolean exists = Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        if (!exists) {
            throw new GuestNotFoundException("Not all guests exist.");
        }
    }

}
