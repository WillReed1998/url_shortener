package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.auth.util.AppMessages;
import cc.worldmandia.security.jwt.JwtServiceImpl;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collections;
import java.util.Optional;

@SpringBootTest()
class AuthenticationServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    private static final String TEST_EMAIL = "example1@gmail.com";
    private static final String TEST_EXIST_EMAIL = "existing@example.com";
    private static final String TEST_NOT_EXIST_EMAIL = "nonexistent@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_CONFIRM_PASSWORD = "Password1";
    private static final String TEST_ENCODE_PASSWORD = "$2a$10$/ip67urSLnmINb9v6/9vKumM2GD3ewCa4VkxdzyotGHUIq69ZQMLa";
    private static final String TEST_USERNAME = "J Doe";
    private static final String TEST_INVALID_PASSWORD = "pass";
    private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBnbWFpbC5jb20iLCJpYXQiOjE3MDQ0MDMzMTZ9.BBIC2stCkuRiL9u6DeyOjdgRWHHCrLLdv1_bJ89-MAM";

    @Test
    @DisplayName("Test successful signup")
    void testSignupSuccessful() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest(TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_CONFIRM_PASSWORD);
        User mockUser = new User();
        mockUser.setEmail(signUpRequest.getEmail());
        mockUser.setUsername(signUpRequest.getUsername());
        mockUser.setPassword(TEST_ENCODE_PASSWORD);

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn(TEST_ENCODE_PASSWORD);
        when(jwtService.generateToken(any())).thenReturn(TEST_TOKEN);

        mockUser.setToken(TEST_TOKEN);
        // Act
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(AppMessages.SUCCESS_MESSAGE, response.getMessage());
        assertEquals(TEST_TOKEN, response.getToken());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Test signup with existing email")
    void testSignupEmailAlreadyExists() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest(TEST_EXIST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // Act
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(AppMessages.EMAIL_ALREADY_EXISTS_MESSAGE, response.getMessage());
        assertNull(response.getToken());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test signup with invalid password")
    void testSignupInvalidPassword() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest(TEST_EMAIL, TEST_USERNAME, TEST_INVALID_PASSWORD, TEST_INVALID_PASSWORD);

        // Act
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(AppMessages.INVALID_PASSWORD_MESSAGE, response.getMessage());
        assertNull(response.getToken());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test successful login")
    void testLoginSuccessful() {
        // Arrange
        LogInRequest request = new LogInRequest(TEST_EMAIL, TEST_PASSWORD);

        User mockUser = new User();
        mockUser.setEmail(TEST_EMAIL);
        mockUser.setPassword(TEST_ENCODE_PASSWORD);
        mockUser.setToken(TEST_TOKEN);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(invocation -> {
                    UsernamePasswordAuthenticationToken token = invocation.getArgument(0);
                    if (TEST_EMAIL.equals(token.getPrincipal()) && TEST_PASSWORD.equals(token.getCredentials())) {
                        return new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
                    }
                    throw new BadCredentialsException("Authentication failed");
                });
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockUser));

        // Act
        JwtAuthenticationResponse response = authenticationService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(AppMessages.SUCCESS_MESSAGE, response.getMessage());
        assertEquals(TEST_TOKEN, response.getToken());

    }

    @Test
    @DisplayName("Test login with user not found")
    void testLoginUserNotFound() {
        // Arrange
        LogInRequest logInRequest = new LogInRequest(TEST_NOT_EXIST_EMAIL, TEST_PASSWORD);

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(Optional.empty());

        // Act
        JwtAuthenticationResponse response = authenticationService.login(logInRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(AppMessages.INVALID_CREDENTIALS_MESSAGE, response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    @DisplayName("Test valid password")
    void testIsValidPasswordValid() {
        assertTrue(authenticationService.isValidPassword("ValidPassword1"));
    }

    @Test
    @DisplayName("Test password length less than required")
    void testIsValidPasswordShort() {
        assertFalse(authenticationService.isValidPassword("Short1"));
    }

    @Test
    @DisplayName("Test password without digits")
    void testIsValidPasswordWithoutDigits() {
        assertFalse(authenticationService.isValidPassword("NoDigits"));
    }

    @Test
    @DisplayName("Test password without lowercase letters")
    void testIsValidPasswordWithoutLowercase() {
        assertFalse(authenticationService.isValidPassword("WITHOUTLOWER1"));
    }

    @Test
    @DisplayName("Test password without uppercase letters")
    void testIsValidPasswordWithoutUppercase() {
        assertFalse(authenticationService.isValidPassword("withoutupper1"));
    }


}
