package cc.worldmandia.database.repository;

import cc.worldmandia.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.urls WHERE u.username = :username")
    Optional<User> findUserWithURLsByUsername(@Param("username") String username);

}
