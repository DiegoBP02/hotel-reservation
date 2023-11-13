package com.bpdev.clientservice.integrationTests;

import com.bpdev.clientservice.DataLoader;
import com.bpdev.clientservice.dtos.GuestRequest;
import com.bpdev.clientservice.dtos.GuestUpdateRequest;
import com.bpdev.clientservice.entities.Guest;
import com.bpdev.clientservice.repositories.GuestRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestIntegrationTest {

    private static final String PATH = "/api/v1/guest";

    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        guestRepository.deleteAll();
    }

    Guest guest = Guest.builder()
            .id(UUID.randomUUID())
            .name("John")
            .surname("Doe")
            .phoneNumber("123456789")
            .email("john.doe@example.com")
            .isChild(false)
            .build();
    GuestRequest guestRequest = GuestRequest.builder()
            .name("John")
            .surname("Doe")
            .phoneNumber("123456789")
            .email("john.doe@example.com")
            .isChild(false)
            .build();
    GuestUpdateRequest guestUpdateRequest = GuestUpdateRequest.builder()
            .name("New John")
            .surname("Doe")
            .phoneNumber("123456789")
            .email("john.doe@example.com")
            .isChild(false)
            .build();

    private void saveGuest() {
        guestRepository.save(guest);
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
    void shouldCreateRoom() throws Exception {
        mockMvc.perform(mockPostRequest(guestRequest))
                .andExpect(status().isCreated());

        assertEquals(1, guestRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        saveGuest();
        String expectedResponseBody = "{\"id\":\"" + guest.getId().toString() + "\",\"name\":\"John\",\"surname\":\"Doe\",\"phoneNumber\":\"123456789\",\"email\":\"john.doe@example.com\",\"child\":false}";

        mockMvc.perform(mockGetRequest("/" + guest.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindByEmail() throws Exception {
        saveGuest();
        String expectedResponseBody = "{\"id\":\"" + guest.getId().toString() + "\",\"name\":\"John\",\"surname\":\"Doe\",\"phoneNumber\":\"123456789\",\"email\":\"john.doe@example.com\",\"child\":false}";

        mockMvc.perform(mockGetRequestParams("email", guest.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindByPhoneNumber() throws Exception {
        saveGuest();
        String expectedResponseBody = "{\"id\":\"" + guest.getId().toString() + "\",\"name\":\"John\",\"surname\":\"Doe\",\"phoneNumber\":\"123456789\",\"email\":\"john.doe@example.com\",\"child\":false}";

        mockMvc.perform(mockGetRequestParams("phoneNumber", guest.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldUpdateById() throws Exception {
        saveGuest();

        mockMvc.perform(mockPatchRequest("/" + guest.getId().toString(), guestUpdateRequest))
                .andExpect(status().isNoContent());

        Optional<Guest> savedGuest = guestRepository.findById(guest.getId());
        assertEquals(guestUpdateRequest.getName(), savedGuest.get().getName());
    }

    @Test
    void shouldDeleteById() throws Exception {
        saveGuest();

        mockMvc.perform(mockDeleteRequest("/" + guest.getId().toString()))
                .andExpect(status().isNoContent());

        assertEquals(0, guestRepository.findAll().size());
    }

}