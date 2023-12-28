package cc.worldmandia.user;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

}
