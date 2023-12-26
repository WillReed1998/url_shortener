package cc.worldmandia.features.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User getById(Long id){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()){
            return null;
        }

        return user.get();
    }

    public void save(User user){
        repository.save(user);
    }
}

