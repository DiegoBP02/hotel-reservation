package com.dbdev.roomservice.integrationTests;

import com.dbdev.roomservice.DataLoader;
import com.dbdev.roomservice.dtos.RoomRequest;
import com.dbdev.roomservice.dtos.RoomUpdateRequest;
import com.dbdev.roomservice.entities.Room;
import com.dbdev.roomservice.entities.enums.RoomStatus;
import com.dbdev.roomservice.entities.enums.RoomType;
import com.dbdev.roomservice.repositories.RoomRepository;
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
class RoomIntegrationTest {

    private static final String PATH = "/api/v1/room";

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        roomRepository.deleteAll();
    }

    Room room = Room.builder()
            .roomNumber("12345")
            .roomType(RoomType.Economy)
            .roomStatus(RoomStatus.Available)
            .beds(2)
            .costPerNight(100)
            .hotelId(UUID.fromString("1c6d4582-0737-4811-b66a-c1be6d5eb418"))
            .build();
    RoomRequest roomRequest = RoomRequest.builder()
            .roomNumber("12345")
            .roomType(RoomType.Economy)
            .roomStatus(RoomStatus.Available)
            .beds(2)
            .costPerNight(100)
            .hotelId(UUID.fromString("1c6d4582-0737-4811-b66a-c1be6d5eb418"))
            .build();
    RoomUpdateRequest roomUpdateRequest = RoomUpdateRequest.builder()
            .roomNumber("54321")
            .roomType(RoomType.Economy)
            .roomStatus(RoomStatus.Available)
            .beds(3)
            .costPerNight(101)
            .build();

    private void saveRoom() {
        roomRepository.save(room);
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

    private MockHttpServletRequestBuilder mockGetRequestParams
            (String param1, String value1, String param2, String value2) throws Exception {
        return MockMvcRequestBuilders
                .get(PATH)
                .param(param1, value1)
                .param(param2, value2)
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
        mockMvc.perform(mockPostRequest(roomRequest))
                .andExpect(status().isCreated());

        assertEquals(1, roomRepository.findAll().size());
    }

    @Test
    void shouldFindById() throws Exception {
        saveRoom();
        String expectedResponseBody = "{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}";

        mockMvc.perform(mockGetRequest("/" + room.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByHotelId() throws Exception {
        saveRoom();
        String expectedResponseBody = "[{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}]";

        mockMvc.perform(mockGetRequestParams("hotelId", room.getHotelId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByHotelIdAndRoomType() throws Exception {
        saveRoom();
        String expectedResponseBody = "[{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}]";

        mockMvc.perform(mockGetRequestParams(
                        "hotelId", room.getHotelId().toString(),
                        "roomType", room.getRoomType().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByHotelIdAndRoomStatus() throws Exception {
        saveRoom();
        String expectedResponseBody = "[{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}]";

        mockMvc.perform(mockGetRequestParams(
                        "hotelId", room.getHotelId().toString(),
                        "roomStatus", room.getRoomStatus().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByHotelIdAndBeds() throws Exception {
        saveRoom();
        String expectedResponseBody = "[{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}]";

        mockMvc.perform(mockGetRequestParams(
                        "hotelId", room.getHotelId().toString(),
                        "beds", String.valueOf(room.getBeds())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldFindAllByHotelIdAndCostPerNightLessThanEqual() throws Exception {
        saveRoom();
        String expectedResponseBody = "[{\"id\":\"" + room.getId().toString() + "\",\"roomNumber\":\"12345\",\"roomType\":\"Economy\",\"roomStatus\":\"Available\",\"beds\":2,\"costPerNight\":100,\"hotelId\":\"1c6d4582-0737-4811-b66a-c1be6d5eb418\",\"reservationId\":null}]";

        mockMvc.perform(mockGetRequestParams(
                        "hotelId", room.getHotelId().toString(),
                        "costPerNight", String.valueOf(room.getCostPerNight())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void shouldUpdateById() throws Exception {
        saveRoom();

        mockMvc.perform(mockPatchRequest("/" + room.getId().toString(), roomUpdateRequest))
                .andExpect(status().isNoContent());

        Optional<Room> savedRoom = roomRepository.findById(room.getId());
        assertEquals(roomUpdateRequest.getRoomNumber(), savedRoom.get().getRoomNumber());
    }

    @Test
    void shouldDeleteById() throws Exception {
        saveRoom();

        mockMvc.perform(mockDeleteRequest("/" + room.getId().toString()))
                .andExpect(status().isNoContent());

        assertEquals(0, roomRepository.findAll().size());
    }

}