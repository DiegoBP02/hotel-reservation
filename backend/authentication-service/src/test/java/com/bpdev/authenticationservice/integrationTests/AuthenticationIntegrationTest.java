package com.bpdev.authenticationservice.integrationTests;

import com.bpdev.authenticationservice.DataLoader;
import com.bpdev.authenticationservice.dtos.LoginRequest;
import com.bpdev.authenticationservice.entities.User;
import com.bpdev.authenticationservice.repositories.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    private static final String PATH = "/api/v1/auth";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DataLoader dataLoader;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    private User user = User.builder()
            .name("user1")
            .password("password")
            .build();
    private LoginRequest loginRequest = LoginRequest.builder()
            .name("user1")
            .password("password")
            .build();

    private User setupUser() {
        return userRepository.findByName(user.getName())
                .orElseGet(() -> userRepository.save(user));
    }

    private MockHttpServletRequestBuilder mockPostRequest
            (String endpoint, Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH + "/" + endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestObject));
    }

    @Test
    void shouldLoginUserAndReturnToken() throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setupUser();

        mockMvc.perform(mockPostRequest("login", loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());

        assertEquals(1, userRepository.findAll().size());
    }

}