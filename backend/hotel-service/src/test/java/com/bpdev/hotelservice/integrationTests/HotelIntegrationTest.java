package com.bpdev.hotelservice.integrationTests;

import com.bpdev.hotelservice.DataLoader;
import com.bpdev.hotelservice.dtos.HotelRequest;
import com.bpdev.hotelservice.dtos.HotelUpdateRequest;
import com.bpdev.hotelservice.entities.Hotel;
import com.bpdev.hotelservice.entities.embedabbles.Address;
import com.bpdev.hotelservice.entities.enums.Amenities;
import com.bpdev.hotelservice.repositories.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelIntegrationTest {

    private static final String PATH = "/api/v1/hotel";

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        hotelRepository.deleteAll();
    }

    Hotel hotel = Hotel.builder()
            .id(UUID.fromString("cf27e4b6-346d-449a-b056-856f3ff2df29"))
            .name("Fake Hotel")
            .address(new Address("123 Main St", "Cityville", "Stateville", "12345"))
            .stars(4)
            .email("fakehotel@example.com")
            .standardCheckoutTime(LocalTime.of(12, 0))
            .amenities(Arrays.asList(Amenities.Wifi, Amenities.Pool))
            .lateCheckoutFee(20)
            .build();
    HotelRequest hotelRequest = HotelRequest.builder()
            .name("Fake Hotel")
            .address(new Address("123 Main St", "Cityville", "Stateville", "12345"))
            .stars(4)
            .email("fakehotel@example.com")
            .standardCheckoutTime(LocalTime.of(12, 0))
            .amenities(Arrays.asList(Amenities.Wifi, Amenities.Pool))
            .lateCheckoutFee(20)
            .build();
    HotelUpdateRequest hotelUpdateRequest = HotelUpdateRequest.builder()
            .name("New Fake Hotel")
            .address(new Address("321 Main St", "Cityville", "Stateville", "54321"))
            .stars(4)
            .email("newFakehotel@example.com")
            .standardCheckoutTime(LocalTime.of(12, 0))
            .amenities(Arrays.asList(Amenities.Bar, Amenities.Laundry))
            .lateCheckoutFee(20)
            .build();

    private void saveHotel() {
        hotelRepository.save(hotel);
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
    void shouldCreateHotel() throws Exception {
        mockMvc.perform(mockPostRequest(hotelRequest))
                .andExpect(status().isCreated());

        assertEquals(1, hotelRepository.findAll().size());
    }

    @Test
    void shouldFindAll() throws Exception {
        saveHotel();
        String expectedResponseBody = "[{\"id\":\"" + hotel.getId().toString() + "\",\"name\":\"Fake Hotel\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Cityville\",\"state\":\"Stateville\",\"zipCode\":\"12345\"},\"stars\":4,\"email\":\"fakehotel@example.com\",\"roomsIds\":null,\"amenities\":[\"Wifi\",\"Pool\"],\"standardCheckoutTime\":\"12:00:00\",\"lateCheckoutFee\":20}]";

        mockMvc.perform(mockGetRequest(""))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindById() throws Exception {
        saveHotel();
        String expectedResponseBody = "{\"id\":\"" + hotel.getId().toString() + "\",\"name\":\"Fake Hotel\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Cityville\",\"state\":\"Stateville\",\"zipCode\":\"12345\"},\"stars\":4,\"email\":\"fakehotel@example.com\",\"roomsIds\":null,\"amenities\":[\"Wifi\",\"Pool\"],\"standardCheckoutTime\":\"12:00:00\",\"lateCheckoutFee\":20}";

        mockMvc.perform(mockGetRequest("/" + hotel.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByStars() throws Exception {
        saveHotel();
        String expectedResponseBody = "[{\"id\":\"" + hotel.getId().toString() + "\",\"name\":\"Fake Hotel\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Cityville\",\"state\":\"Stateville\",\"zipCode\":\"12345\"},\"stars\":4,\"email\":\"fakehotel@example.com\",\"roomsIds\":null,\"amenities\":[\"Wifi\",\"Pool\"],\"standardCheckoutTime\":\"12:00:00\",\"lateCheckoutFee\":20}]";

        mockMvc.perform(mockGetRequest("/stars/" + hotel.getStars()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByName() throws Exception {
        saveHotel();
        String expectedResponseBody = "[{\"id\":\"" + hotel.getId().toString() + "\",\"name\":\"Fake Hotel\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Cityville\",\"state\":\"Stateville\",\"zipCode\":\"12345\"},\"stars\":4,\"email\":\"fakehotel@example.com\",\"roomsIds\":null,\"amenities\":[\"Wifi\",\"Pool\"],\"standardCheckoutTime\":\"12:00:00\",\"lateCheckoutFee\":20}]";

        mockMvc.perform(mockGetRequest("/name/" + hotel.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldUpdateById() throws Exception {
        saveHotel();

        mockMvc.perform(mockPatchRequest("/" + hotel.getId().toString(), hotelUpdateRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteById() throws Exception {
        saveHotel();

        mockMvc.perform(mockDeleteRequest("/" + hotel.getId().toString()))
                .andExpect(status().isNoContent());

        assertEquals(0, hotelRepository.findAll().size());
    }

}