package cc.worldmandia.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);

    @Test
    public void testSaveUser() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testUser")
                .password("testPassword")
                .token("testToken")
                .build();

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.save(user);

        assertEquals(user, savedUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testFindById() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .username("testUser")
                .password("testPassword")
                .token("testToken")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(userId);

        assertEquals(user, foundUser);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }

    @Test
    public void testFindAll() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder()
                .id(1L)
                .email("test1@example.com")
                .username("testUser1")
                .password("password1")
                .token("token1")
                .build());
        userList.add(User.builder()
                .id(2L)
                .email("test2@example.com")
                .username("testUser2")
                .password("password2")
                .token("token2")
                .build());

        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.findAll();

        assertEquals(userList, foundUsers);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        Long userId = 1L;

        userService.deleteById(userId);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        User user = User.builder()
                .id(1L)
                .email(email)
                .username("testUser")
                .password("testPassword")
                .token("testToken")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);

        assertEquals(user, foundUser);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
    }
}