package com.bpdev.reservationservice.integrationTests;

import com.bpdev.reservationservice.DataLoader;
import com.bpdev.reservationservice.dtos.ReservationRequest;
import com.bpdev.reservationservice.dtos.ReservationUpdateRequest;
import com.bpdev.reservationservice.entities.Reservation;
import com.bpdev.reservationservice.entities.embedabbles.ReservationDate;
import com.bpdev.reservationservice.entities.enums.ReservationStatus;
import com.bpdev.reservationservice.repositories.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTest {

    private static final String PATH = "/api/v1/reservation";

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        reservationRepository.deleteAll();
    }

    Reservation reservation = Reservation.builder()
            .reservationStatus(ReservationStatus.CONFIRMED)
            .reservationDate(createReservationDate())
            .guestsIds(List.of(UUID.fromString("958471eb-7979-4759-a08e-823e5a954a14")))
            .paymentId(UUID.fromString("ad3dcfaa-84df-41d6-89a9-03d10265c250"))
            .roomId(UUID.fromString("fe6cb857-7e7e-4f33-966a-8d7aa1e80af4"))
            .amount(100)
            .createdTime(LocalDateTime.of(2020,10,19,10,10,10))
            .build();
    ReservationRequest reservationRequest = ReservationRequest.builder()
            .reservationStatus(ReservationStatus.CONFIRMED)
            .reservationDate(createReservationDate())
            .guestsIds(List.of(UUID.fromString("958471eb-7979-4759-a08e-823e5a954a14")))
            .paymentId(UUID.fromString("ad3dcfaa-84df-41d6-89a9-03d10265c250"))
            .roomId(UUID.fromString("fe6cb857-7e7e-4f33-966a-8d7aa1e80af4"))
            .amount(100)
            .build();
    ReservationUpdateRequest reservationUpdateRequest = ReservationUpdateRequest.builder()
            .reservationStatus(ReservationStatus.CONFIRMED)
            .reservationDate(createReservationDate())
            .guestsIds(List.of(UUID.fromString("958471eb-7979-4759-a08e-823e5a954a14")))
            .paymentId(UUID.fromString("ad3dcfaa-84df-41d6-89a9-03d10265c250"))
            .amount(150)
            .build();

    private ReservationDate createReservationDate() {
        return ReservationDate.builder()
                .checkInDate(LocalDateTime.of(2020,10,20,10,10,10))
                .checkOutDate(LocalDateTime.of(2020,10,21,10,10,10))
                .estimatedCheckOutTime(LocalDateTime.of(2020,10,21,10,10,10))
                .lateCheckout(false)
                .build();
    }

    private void saveReservation() {
        reservationRepository.save(reservation);
    }

    private MockHttpServletRequestBuilder mockPostRequest
            (Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    private MockHttpServletRequestBuilder mockGetRequest
            (String endpoint) throws Exception {
        return MockMvcRequestBuilders
                .get(PATH + endpoint)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder mockGetRequestParams
            (String param, String value) throws Exception {
        return MockMvcRequestBuilders
                .get(PATH)
                .param(param, value)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder mockPatchRequest
            (String endpoint, Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .patch(PATH + endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    private MockHttpServletRequestBuilder mockDeleteRequest
            (String endpoint) throws Exception {
        return MockMvcRequestBuilders
                .delete(PATH + endpoint)
                .accept(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldCreateReservation() throws Exception {
        mockMvc.perform(mockPostRequest(reservationRequest))
                .andExpect(status().isCreated());

        assertEquals(1, reservationRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        saveReservation();
        String expectedResponseBody = "[{\"id\":\"" + reservation.getId().toString() + "\",\"reservationStatus\":\"CONFIRMED\",\"reservationDate\":{\"checkInDate\":\"2020-10-20T10:10:10\",\"checkOutDate\":\"2020-10-21T10:10:10\",\"estimatedCheckOutTime\":\"2020-10-21T10:10:10\",\"lateCheckout\":false},\"guestsIds\":[\"958471eb-7979-4759-a08e-823e5a954a14\"],\"paymentId\":\"ad3dcfaa-84df-41d6-89a9-03d10265c250\",\"roomId\":\"fe6cb857-7e7e-4f33-966a-8d7aa1e80af4\",\"amount\":100,\"reservationAddOns\":[],\"createdTime\":\"2020-10-19T10:10:10\"}]";

        mockMvc.perform(mockGetRequest(""))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindById() throws Exception {
        saveReservation();
        String expectedResponseBody = "{\"id\":\"" + reservation.getId().toString() + "\",\"reservationStatus\":\"CONFIRMED\",\"reservationDate\":{\"checkInDate\":\"2020-10-20T10:10:10\",\"checkOutDate\":\"2020-10-21T10:10:10\",\"estimatedCheckOutTime\":\"2020-10-21T10:10:10\",\"lateCheckout\":false},\"guestsIds\":[\"958471eb-7979-4759-a08e-823e5a954a14\"],\"paymentId\":\"ad3dcfaa-84df-41d6-89a9-03d10265c250\",\"roomId\":\"fe6cb857-7e7e-4f33-966a-8d7aa1e80af4\",\"amount\":100,\"reservationAddOns\":[],\"createdTime\":\"2020-10-19T10:10:10\"}";

        mockMvc.perform(mockGetRequest("/" + reservation.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindByPaymentId() throws Exception {
        saveReservation();
        String expectedResponseBody = "{\"id\":\"" + reservation.getId().toString() + "\",\"reservationStatus\":\"CONFIRMED\",\"reservationDate\":{\"checkInDate\":\"2020-10-20T10:10:10\",\"checkOutDate\":\"2020-10-21T10:10:10\",\"estimatedCheckOutTime\":\"2020-10-21T10:10:10\",\"lateCheckout\":false},\"guestsIds\":[\"958471eb-7979-4759-a08e-823e5a954a14\"],\"paymentId\":\"ad3dcfaa-84df-41d6-89a9-03d10265c250\",\"roomId\":\"fe6cb857-7e7e-4f33-966a-8d7aa1e80af4\",\"amount\":100,\"reservationAddOns\":[],\"createdTime\":\"2020-10-19T10:10:10\"}";

        mockMvc.perform(mockGetRequestParams("paymentId", reservation.getPaymentId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByRoomId() throws Exception {
        saveReservation();
        String expectedResponseBody = "[{\"id\":\"" + reservation.getId().toString() + "\",\"reservationStatus\":\"CONFIRMED\",\"reservationDate\":{\"checkInDate\":\"2020-10-20T10:10:10\",\"checkOutDate\":\"2020-10-21T10:10:10\",\"estimatedCheckOutTime\":\"2020-10-21T10:10:10\",\"lateCheckout\":false},\"guestsIds\":[\"958471eb-7979-4759-a08e-823e5a954a14\"],\"paymentId\":\"ad3dcfaa-84df-41d6-89a9-03d10265c250\",\"roomId\":\"fe6cb857-7e7e-4f33-966a-8d7aa1e80af4\",\"amount\":100,\"reservationAddOns\":[],\"createdTime\":\"2020-10-19T10:10:10\"}]";

        mockMvc.perform(mockGetRequestParams("roomId", reservation.getRoomId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByGuestId() throws Exception {
        saveReservation();
        String expectedResponseBody = "[{\"id\":\"" + reservation.getId().toString() + "\",\"reservationStatus\":\"CONFIRMED\",\"reservationDate\":{\"checkInDate\":\"2020-10-20T10:10:10\",\"checkOutDate\":\"2020-10-21T10:10:10\",\"estimatedCheckOutTime\":\"2020-10-21T10:10:10\",\"lateCheckout\":false},\"guestsIds\":[\"958471eb-7979-4759-a08e-823e5a954a14\"],\"paymentId\":\"ad3dcfaa-84df-41d6-89a9-03d10265c250\",\"roomId\":\"fe6cb857-7e7e-4f33-966a-8d7aa1e80af4\",\"amount\":100,\"reservationAddOns\":[],\"createdTime\":\"2020-10-19T10:10:10\"}]";

        mockMvc.perform(mockGetRequestParams
                        ("guestsIds", "958471eb-7979-4759-a08e-823e5a954a14"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldUpdateById() throws Exception {
        saveReservation();

        mockMvc.perform(mockPatchRequest("/" + reservation.getId().toString(),
                        reservationUpdateRequest))
                .andExpect(status().isNoContent());

        Optional<Reservation> savedReservation
                = reservationRepository.findById(reservation.getId());
        assertEquals(reservationUpdateRequest.getAmount(), savedReservation.get().getAmount());
    }

    @Test
    void shouldDeleteById() throws Exception {
        saveReservation();

        mockMvc.perform(mockDeleteRequest("/" + reservation.getId().toString()))
                .andExpect(status().isNoContent());

        assertEquals(0, reservationRepository.findAll().size());
    }

}