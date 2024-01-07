package cc.worldmandia.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest()
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserWithURLsByUsername() {

        User userWithUrls = new User();
        userWithUrls.setUsername("testuser");

        Optional<User> optionalUser = Optional.of(userWithUrls);

        when(userRepository.findUserWithURLsByUsername("testuser")).thenReturn(optionalUser);

        Optional<User> user = userRepository.findUserWithURLsByUsername("testuser");

        verify(userRepository, times(1)).findUserWithURLsByUsername("testuser");

        assertEquals(optionalUser, user);
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(optionalUser);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");

        assertEquals(optionalUser, foundUser);
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        Boolean exists = userRepository.existsByEmail("test@example.com");

        verify(userRepository, times(1)).existsByEmail("test@example.com");

        assertTrue(exists);
    }
}
