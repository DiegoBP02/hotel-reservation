package com.bpdev.reservationservice.integrationTests;

import com.bpdev.reservationservice.DataLoader;
import com.bpdev.reservationservice.dtos.ReservationAddOnRequest;
import com.bpdev.reservationservice.dtos.ReservationAddOnUpdateRequest;
import com.bpdev.reservationservice.entities.ReservationAddOn;
import com.bpdev.reservationservice.repositories.ReservationAddOnRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationAddOnIntegrationTest {

    private static final String PATH = "/api/v1/reservationAddOn";

    @Autowired
    private ReservationAddOnRepository reservationAddOnRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        reservationAddOnRepository.deleteAll();
    }

    ReservationAddOn reservationAddOn = ReservationAddOn.builder()
            .costPerNight(20)
            .name("WiFi Access")
            .build();
    ReservationAddOnRequest reservationAddOnRequest = ReservationAddOnRequest.builder()
            .costPerNight(20)
            .name("WiFi Access")
            .build();
    ReservationAddOnUpdateRequest reservationAddOnUpdateRequest = ReservationAddOnUpdateRequest.builder()
            .costPerNight(30)
            .name("Gym Access")
            .build();

    private void saveReservationAddOn() {
        reservationAddOnRepository.save(reservationAddOn);
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
    void shouldCreateReservationAddOn() throws Exception {
        mockMvc.perform(mockPostRequest(reservationAddOnRequest))
                .andExpect(status().isCreated());

        assertEquals(1, reservationAddOnRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        saveReservationAddOn();
        String expectedResponseBody = "[{\"id\":\"" + reservationAddOn.getId().toString() + "\",\"costPerNight\":20,\"name\":\"WiFi Access\"}]";

        mockMvc.perform(mockGetRequest(""))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindById() throws Exception {
        saveReservationAddOn();
        String expectedResponseBody = "{\"id\":\"" + reservationAddOn.getId().toString() + "\",\"costPerNight\":20,\"name\":\"WiFi Access\"}";

        mockMvc.perform(mockGetRequest("/" + reservationAddOn.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldUpdateById() throws Exception {
        saveReservationAddOn();

        mockMvc.perform(mockPatchRequest("/" + reservationAddOn.getId().toString(), reservationAddOnUpdateRequest))
                .andExpect(status().isNoContent());

        Optional<ReservationAddOn> savedReservationAddOn
                = reservationAddOnRepository.findById(reservationAddOn.getId());
        assertEquals(reservationAddOnUpdateRequest.getName(), savedReservationAddOn.get().getName());
    }

    @Test
    void shouldDeleteById() throws Exception {
        saveReservationAddOn();

        mockMvc.perform(mockDeleteRequest("/" + reservationAddOn.getId().toString()))
                .andExpect(status().isNoContent());

        assertEquals(0, reservationAddOnRepository.findAll().size());
    }

}