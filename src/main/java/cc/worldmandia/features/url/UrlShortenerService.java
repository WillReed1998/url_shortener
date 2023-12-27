package cc.worldmandia.features.url;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final ShortUrlUtil shortUrlUtil;

    public UrlDto createShortUrl(UrlDto urlDto) {
        String fullUrl = urlDto.getFullUrl();

        Url existingUrl = urlRepository.findByFullUrl(fullUrl);

        if (existingUrl  != null) {
            return UrlDto.from(existingUrl);
        } else {
            String newKey = shortUrlUtil.generateUniqueKey();
            Url newUrl = new Url();
            newUrl.setFullUrl(fullUrl);
            newUrl.setShortUrl(newKey);
            urlRepository.save(newUrl);

            return UrlDto.from(newUrl);

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