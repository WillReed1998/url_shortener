package cc.worldmandia.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    @Query("SELECT u FROM Url u JOIN FETCH u.users WHERE u.shortUrl = :shortURL")
    Optional<Url> findURLWithUsersByShortURL(@Param("shortURL") String shortURL);

    Url findByFullUrl(String fullUrl);

    Url findByShortUrl(String shortUrl);
}

