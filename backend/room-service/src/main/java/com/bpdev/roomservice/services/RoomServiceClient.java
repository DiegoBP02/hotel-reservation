package com.bpdev.roomservice.services;

import com.bpdev.roomservice.exceptions.HotelNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class RoomServiceClient {

    private final String ROOM_SERVICE_URL = "http://HOTEL-SERVICE/api/v1/hotel/";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void checkIfHotelExists(UUID hotelId) {
        String url = UriComponentsBuilder
                .fromHttpUrl(ROOM_SERVICE_URL + hotelId + "/exists")
                .toUriString();

        boolean exists = Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        if (!exists) {
            throw new HotelNotFoundException("Hotel not found. ID: " + hotelId);
        }
    }

}
