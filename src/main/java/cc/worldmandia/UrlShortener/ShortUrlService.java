package cc.worldmandia.UrlShortener;

import cc.worldmandia.url.URL;
import cc.worldmandia.url.URLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final URLRepository urlRepository;
    private final ShortUrlUtil shortUrlUtil;

    public URL createShortUrl(URL url) {
        String fullUrl = url.getFullUrl();

        URL existingUrl = urlRepository.findByFullUrl(fullUrl);

        if (existingUrl  != null) {
            return existingUrl;
        } else {
            String newShortUrl = shortUrlUtil.generateUniqueKey();
            URL newUrl = new URL();
            newUrl.setFullUrl(fullUrl);
            newUrl.setShortUrl(newShortUrl);
            urlRepository.save(newUrl);

            return newUrl;

        }
    }

    public String getFullUrl(String shortUrl) {
        URL url = urlRepository.findByShortUrl(shortUrl);

        if (url != null) {
            return url.getFullUrl();
        } else {
            return null;
        }
    }
}