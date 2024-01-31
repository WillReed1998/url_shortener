package cc.worldmandia.security.auth;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest()
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private User user;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";


    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);

    }

    @Test
    @DisplayName("Load user by email - User found")
    void testLoadUserByUsername_UserFound() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        // Act
        UserDetails loadedUserDetails = customUserDetailsService.loadUserByUsername(TEST_EMAIL);

        // Assert
        assertNotNull(loadedUserDetails);
        assertEquals(TEST_EMAIL, loadedUserDetails.getUsername());
        assertEquals(TEST_PASSWORD, loadedUserDetails.getPassword());

        // Verify that the repository method was called
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Load user by email - User not found")
    void testLoadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertAll("User not found",
                () -> assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(TEST_EMAIL)),
                () -> verify(userRepository, times(1)).findByEmail(TEST_EMAIL)
        );
    }
}