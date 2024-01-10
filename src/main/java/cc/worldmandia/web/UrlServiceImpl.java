package cc.worldmandia.web;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserServiceImpl;
import cc.worldmandia.util.UrlShortener.ShortUrlUtil;
import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlServiceImpl {

    private final UrlRepository urlRepository;
    private final ShortUrlUtil shortUrlUtil;
    private final UserServiceImpl userService;

    public void createUrl(Url newUrl, String email) {
        newUrl.setShortUrl("http://url-shortener/" + shortUrlUtil.generateUniqueKey());
        newUrl.setEnabled(true);
        User foundUser = userService.findByEmail(email);
        newUrl.setUser(foundUser);
        urlRepository.save(newUrl);
    }

    public boolean exists(long id) {
        if (id == 0) {
            return false;
        }
        return urlRepository.existsById(id);
    }

    @Cacheable(value = "urlCache")
    public Url findById(Long id) {
        exists(id);
        return urlRepository.findById(id).orElse(null);
    }

    public Url updateTitleOrDescription(Url url) {
        long id = url.getId();
        Url foundUrl = findById(id);
        foundUrl.setTitle(url.getTitle());
        foundUrl.setDescription(url.getDescription());
        foundUrl.setFullUrl(url.getFullUrl());
        return urlRepository.save(foundUrl);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public List<Url> findAllByUser(User user) {
        return urlRepository.findAllByUser(user);
    }

    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

    @Cacheable(value = "urlCache")
    public Url findURLWithUsersByShortURL(String shortURL) {
        return urlRepository.findURLWithUsersByShortURL(shortURL).orElse(null);
    }

    public void incrementClickCount(Long id) {
        urlRepository.incrementClickCountById(id);
    }

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

    public Url prolongEndDate(Url url) {
        long id = url.getId();
        Url foundUrl = findById(id);
        LocalDateTime prolongDate = foundUrl.getEndAt().plusDays(15);
        foundUrl.setEndAt(prolongDate);
        return urlRepository.save(foundUrl);
    }

    public RedirectView getFullUrl(String shortUrl) {
        Url entity = urlRepository.findByShortUrl(shortUrl);
        if (entity != null && entity.isEnabled()) {
            urlRepository.incrementClickCountById(entity.getId());
            return new RedirectView(entity.getFullUrl());
        }
        return new RedirectView("/error");
    }
}
