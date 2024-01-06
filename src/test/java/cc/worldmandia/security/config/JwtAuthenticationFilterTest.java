package cc.worldmandia.security.config;

import cc.worldmandia.security.auth.CustomUserDetails;
import cc.worldmandia.security.auth.CustomUserDetailsService;
import cc.worldmandia.security.jwt.JwtServiceImpl;
import cc.worldmandia.user.User;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String VALID_TOKEN = "validToken";
    private static final String INVALID_TOKEN = "invalidToken";


    @BeforeEach
    public void init() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setEmail(TEST_PASSWORD);
        userDetails = new CustomUserDetails(user);

    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void testNoAuthenticationHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testInvalidAuthenticationHeaderFormat() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, "invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testValidAuthenticationHeaderWithValidToken() throws Exception {

        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(customUserDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails)).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, JwtAuthenticationFilter.TOKEN_PREFIX + VALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    void testValidAuthenticationHeaderWithInvalidToken() throws Exception {

        when(jwtService.extractUserName(INVALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(customUserDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(INVALID_TOKEN, userDetails)).thenReturn(false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtAuthenticationFilter.TOKEN_HEADER, JwtAuthenticationFilter.TOKEN_PREFIX + INVALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}