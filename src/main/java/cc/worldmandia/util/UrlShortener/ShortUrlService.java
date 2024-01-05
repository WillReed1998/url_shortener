package cc.worldmandia.util.UrlShortener;

import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final UrlRepository urlRepository;
    private final ShortUrlUtil shortUrlUtil;

    public Url createShortUrl(Url url) {
        String fullUrl = url.getFullUrl();

        Url existingUrl = urlRepository.findByFullUrl(fullUrl);

        if (existingUrl  != null) {
            return existingUrl;
        } else {
            String newShortUrl = shortUrlUtil.generateUniqueKey();
            Url newUrl = new Url();
            newUrl.setFullUrl(fullUrl);
            newUrl.setShortUrl(newShortUrl);
            urlRepository.save(newUrl);

            return newUrl;

        }
    }

    public String getFullUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);

        if (url != null) {
            return url.getFullUrl();
        } else {
            return null;
        }
    }
}