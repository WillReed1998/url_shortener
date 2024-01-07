package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.auth.util.AppMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationServiceImpl authenticationService;
    private static final String TEST_EMAIL = "example@test.com";
    private static final String TEST_USERNAME = "TestUser";
    private static final String TEST_PASSWORD = "Password123";
    private static final String TEST_CONFIRM_PASSWORD = "Password123";
    private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGFtcGxlMkBnbWFpbC5jb20iLCJpYXQiOjE3MDQ0MDM0Nzh9" +
            ".VR8H8P_TMrpW7u4HYwK4U5ehKQpHQw81JwWF--75pIE";


    @Test
    @DisplayName("Test signup endpoint with valid request")
    void testSignupEndpoint() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest(TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_CONFIRM_PASSWORD);

        JwtAuthenticationResponse mockResponse = JwtAuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message(AppMessages.SUCCESS_MESSAGE)
                .token(TEST_TOKEN)
                .build();

        when(authenticationService.signup(signUpRequest)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(AppMessages.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.token").value(TEST_TOKEN));

        verify(authenticationService, times(1)).signup(signUpRequest);
    }

    @Test
    @DisplayName("Test login endpoint with valid request")
    void testLoginEndpoint() throws Exception {
        LogInRequest logInRequest = new LogInRequest(TEST_EMAIL, TEST_PASSWORD);

        JwtAuthenticationResponse mockResponse = JwtAuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message(AppMessages.SUCCESS_MESSAGE)
                .token(TEST_TOKEN)
                .build();

        when(authenticationService.login(logInRequest)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(logInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(AppMessages.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.token").value(TEST_TOKEN));

        verify(authenticationService, times(1)).login(logInRequest);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
