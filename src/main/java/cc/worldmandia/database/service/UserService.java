package cc.worldmandia.database.service;

import cc.worldmandia.database.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

}
