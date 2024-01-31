package cc.worldmandia.web;

import cc.worldmandia.url.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/url-shortener-main")
public class MainController {
    private final UrlServiceImpl urlService;
    private String timeUrl;

    @GetMapping()
    public static String start() {
        return "main";
    }

    @PostMapping("/ad")
    public String getAd(@RequestParam("shortUrl") String shortUrl) {
        timeUrl = shortUrl;
        return "ad.html";
    }

    @PostMapping("/goToUrl")
    public String go() {
        Url url = urlService.findURLWithUsersByShortURL(timeUrl);
        if (url != null && url.isEnabled()) {
            urlService.incrementClickCount(url.getId());
            return "redirect:" + url.getFullUrl();
        }
        return "404";
    }
}
