package cc.worldmandia.user;

import cc.worldmandia.url.Url;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
public class UserTest {

    @Mock
    private Url mockUrl;

    @Test
    public void testUserCreation() {
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password123")
                .token("token123")
                .urls(Collections.singletonList(mockUrl))
                .build();

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("token123", user.getToken());

        List<Url> urls = user.getUrls();
        assertNotNull(urls);
        assertEquals(1, urls.size());
        assertEquals(mockUrl, urls.get(0));
    }
}
