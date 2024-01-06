package cc.worldmandia.security.auth;

import cc.worldmandia.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest()
class CustomUserDetailsTest {
    @Mock
    private User mockUser;
    @InjectMocks
    CustomUserDetails customUserDetails;
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "Password1";

    @BeforeEach
    public void init() {
        mockUser = new User();
        mockUser.setEmail(TEST_EMAIL);
        mockUser.setPassword(TEST_PASSWORD);
        customUserDetails = new CustomUserDetails(mockUser);

    }

    @Test
    void getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals(new SimpleGrantedAuthority("ROLE_USER"), authorities.iterator().next());
    }

    @Test
    void getPassword() {

        String result = customUserDetails.getPassword();

        assertEquals(TEST_PASSWORD, result);
    }

    @Test
    void getUsername() {

        String result = customUserDetails.getUsername();

        assertEquals(TEST_EMAIL, result);
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }


}