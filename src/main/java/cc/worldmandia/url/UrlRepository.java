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

    @Query("SELECT u FROM Url u JOIN FETCH u.users WHERE u.shortUrl = :shortURL")
    Optional<Url> findURLWithUsersByShortURL(@Param("shortURL") String shortURL);

    Url findByFullUrl(String fullUrl);

    Url findByShortUrl(String shortUrl);


    //select *
    //from test.urls u
    //join test.users_urls uu  on u.id = uu.url_id
    //join test.users us on uu.user_id = us.id
    //where us.id = :userId;
    //@Query("select * from Url u join u.users us where us.id = :userId")
    //List<Url> getUserUrls(@Param("userId") Long userId);

    @Query("SELECT u FROM Url u JOIN u.users us WHERE us.id = :userId")
    List<Url> getUserUrls(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.id = :id")
    void incrementClickCountById(Long id);
}

