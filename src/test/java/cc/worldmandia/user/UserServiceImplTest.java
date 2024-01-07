package cc.worldmandia.user;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest()
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);
        assertNotNull(foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User foundUser = userService.findById(1L);
        assertNull(foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();
        assertEquals(0, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindUserWithURLsByUsername() {
        String username = "testUser";
        User user = new User();
        when(userRepository.findUserWithURLsByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserWithURLsByUsername(username);
        assertNotNull(foundUser);
        verify(userRepository, times(1)).findUserWithURLsByUsername(username);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);
        assertNotNull(foundUser);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmail_NotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        User foundUser = userService.findByEmail(email);
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail(email);
    }
}
