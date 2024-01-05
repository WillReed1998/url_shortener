package cc.worldmandia.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    //@Query("SELECT u FROM Url u JOIN FETCH u.users WHERE u.shortUrl = :shortURL")
    @Query("SELECT u FROM Url u WHERE u.shortUrl = :shortURL")
    Optional<Url> findURLWithUsersByShortURL(@Param("shortURL") String shortURL);

    Url findByFullUrl(String fullUrl);

    Url findByShortUrl(String shortUrl);

    @Query(nativeQuery = true, value = "SELECT * FROM urls u WHERE u.id_user = :id")
    List<Url> getUserUrls(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.id = :id")
    void incrementClickCountById(Long id);
}

