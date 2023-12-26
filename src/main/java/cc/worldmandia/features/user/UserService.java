package cc.worldmandia.features.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void add(UserDto userDto){
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setToken(userDto.getToken());

        repository.save(user);
    }

    public List<UserDto> getList(){
        return UserDto.from(repository.findAll());
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public void update(UserDto userDto){
        User user = repository.findById(userDto.getId()).orElseThrow();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setToken(userDto.getToken());

        repository.save(user);
    }


}

