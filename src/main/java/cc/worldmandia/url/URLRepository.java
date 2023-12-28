package cc.worldmandia.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<URL, Long> {

    @Query("SELECT u FROM URL u JOIN FETCH u.users WHERE u.shortUrl = :shortURL")
    Optional<URL> findURLWithUsersByShortURL(@Param("shortURL") String shortURL);

    URL findByFullUrl(String fullUrl);

    URL findByShortUrl(String shortUrl);
}

