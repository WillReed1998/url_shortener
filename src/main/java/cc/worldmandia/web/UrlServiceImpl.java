package cc.worldmandia.web;


import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public Url save(Url url) {
        url.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        url.setClickCount(0);
        url.setShortUrl("shortUrl");
        url.setEnabled(true);

        Instant endAtInstant = Instant.now().plus(30, ChronoUnit.DAYS);
        url.setEndAt(new Timestamp(endAtInstant.toEpochMilli()));
        return urlRepository.save(url);
    }

    @Override
    public Url findById(Long id) {
        return urlRepository.findById(id).orElse(null);
    }

    @Override
    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

    public Url findURLWithUsersByShortURL(String shortURL) {
        return urlRepository.findURLWithUsersByShortURL(shortURL).orElse(null);
    }

    //add method to increment click_counter
    //add method to update "enable" field in urls' entity
}
