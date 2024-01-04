package cc.worldmandia.web;


import cc.worldmandia.UrlShortener.ShortUrlService;
import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UrlServiceImpl{

    private final UrlRepository urlRepository;
    private final ShortUrlService shortUrlService;

    public Url save(Url url) {
     //   url.setCreatedDate(new Timestamp(Instant.now().toEpochMilli()));
        url.setClickCount(0);
        url.setShortUrl(String.valueOf(shortUrlService.createShortUrl(url)));
        url.setEnabled(true);

      //  Instant endAtInstant = Instant.now().plus(30, ChronoUnit.DAYS);
     //   url.setEndAt(new Timestamp(endAtInstant.toEpochMilli()));
        return urlRepository.save(url);
    }

    public boolean exists(long id){
        if (id == 0){
            return false;
        }
        return urlRepository.existsById(id);
    }

    public Url findById(Long id) {
        exists(id);
        return urlRepository.findById(id).orElse(null);
    }

    public void updateTitleOrDescription(Url url) {

    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

    public Url findURLWithUsersByShortURL(String shortURL) {
        return urlRepository.findURLWithUsersByShortURL(shortURL).orElse(null);
    }

    public void incrementClickCount(Long id) {
        urlRepository.incrementClickCountById(id);
    }
    //add method to update "enable" field in urls' entity


    public Url updateEnabledStatus(Url url) {
        long id = url.getId();
        Url foundUrl = findById(id);
        if (foundUrl.isEnabled()) {
            foundUrl.setEnabled(false);
        } else {
            foundUrl.setEnabled(true);
        }
        return urlRepository.save(foundUrl);
    }
}
