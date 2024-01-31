package cc.worldmandia.security.config;

import cc.worldmandia.security.auth.CustomUserDetails;
import cc.worldmandia.security.auth.CustomUserDetailsService;
import cc.worldmandia.security.jwt.JwtServiceImpl;
import cc.worldmandia.user.User;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthenticationFilterTest {
    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    private CustomUserDetails userDetails;
    private static final String TEST_EMAIL = "test@mail.com";
    private static final String TEST_PASSWORD = "pass1worD";
    private static final String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGFtcGxlM0BnbWFpbC5jb20iLCJpYXQiOjE3MDQ0MDM1NDR9." +
            "ES7gFcJQCSgKIynIJSHwYDPs_9wurvnnooRYxdQgiPs";
    private static final String TEST_INVALID_TOKEN = "invalidToken";

    @BeforeEach
    public void init() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        userDetails = new CustomUserDetails(user);

    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Test no authentication header")
    void testNoAuthenticationHeader() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Test invalid authentication header format")
    void testInvalidAuthenticationHeaderFormat() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, TEST_INVALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Test valid authentication header with valid token")
    void testValidAuthenticationHeaderWithValidToken() throws Exception {
        // Arrange
        when(jwtService.extractUserName(TEST_VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(customUserDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TEST_VALID_TOKEN, userDetails)).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, JwtAuthenticationFilter.TOKEN_PREFIX + TEST_VALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    @DisplayName("Test valid authentication header with invalid token")
    void testValidAuthenticationHeaderWithInvalidToken() throws Exception {
        // Arrange
        when(jwtService.extractUserName(TEST_INVALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(customUserDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(TEST_INVALID_TOKEN, userDetails)).thenReturn(false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, JwtAuthenticationFilter.TOKEN_PREFIX + TEST_INVALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}