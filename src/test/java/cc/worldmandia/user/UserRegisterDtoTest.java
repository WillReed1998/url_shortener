package cc.worldmandia.user;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class UserRegisterDtoTest {

    @Test
    public void testUserRegisterDtoNotNull() {
        UserRegisterDto user = new UserRegisterDto("test@example.com", "username", "password", "password");
        assertNotNull(user);
    }

    @Test
    public void testUserRegisterDtoValues() {
        UserRegisterDto user = new UserRegisterDto("test@example.com", "username", "password", "password");
        assertEquals("test@example.com", user.getEmail());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("password", user.getRepeatedPassword());
    }

    @Test
    public void testUserRegisterDtoEmptyConstructor() {
        UserRegisterDto user = new UserRegisterDto();
        assertNull(user.getEmail());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getRepeatedPassword());
    }

    @Test
    public void testUserRegisterDtoSetterMethods() {
        UserRegisterDto user = new UserRegisterDto();
        user.setEmail("test@example.com");
        user.setUsername("username");
        user.setPassword("password");
        user.setRepeatedPassword("password");

        assertEquals("test@example.com", user.getEmail());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("password", user.getRepeatedPassword());
    }

    @Test
    public void testUserRegisterDtoEqualsAndHashCode() {
        UserRegisterDto user1 = new UserRegisterDto("test@example.com", "username", "password", "password");
        UserRegisterDto user2 = new UserRegisterDto("test@example.com", "username", "password", "password");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
