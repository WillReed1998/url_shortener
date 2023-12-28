package cc.worldmandia.url;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class URLServiceImpl implements URLService {

    private final URLRepository urlRepository;

    @Override
    public URL save(URL url) {
        url.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        url.setClickCount(0);
        url.setShortUrl("shortUrl");
        url.setEnabled(true);

        Instant endAtInstant = Instant.now().plus(30, ChronoUnit.DAYS);
        url.setEndAt(new Timestamp(endAtInstant.toEpochMilli()));
        return urlRepository.save(url);
    }

    @Override
    public URL findById(Long id) {
        return urlRepository.findById(id).orElse(null);
    }

    @Override
    public List<URL> findAll() {
        return urlRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

    public URL findURLWithUsersByShortURL(String shortURL) {
        return urlRepository.findURLWithUsersByShortURL(shortURL).orElse(null);
    }

}
