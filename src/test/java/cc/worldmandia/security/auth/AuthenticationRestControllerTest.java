package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
class AuthenticationRestControllerTest {
    @Mock
    private AuthenticationServiceImpl authenticationService;
    @InjectMocks
    private AuthenticationRestController authenticationController;

    @Test
    void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest();
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse();

        assertNotNull(authenticationService, "authenticationService is null");

        when(authenticationService.signup(signUpRequest)).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signup(signUpRequest);
        verify(authenticationService).signup(signUpRequest);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogin() {
        LogInRequest logInRequest = new LogInRequest();
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse();

        assertNotNull(authenticationService, "authenticationService is null");

        when(authenticationService.login(logInRequest)).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.login(logInRequest);
        verify(authenticationService).login(logInRequest);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}