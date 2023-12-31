package cc.worldmandia.restApi;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRestService {
    private final UserRepository repository;

    public User findByUsername(Long id) {
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    public void saveUser(User user){
        repository.save(user);
    }
}
