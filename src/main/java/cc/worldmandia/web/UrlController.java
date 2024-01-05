package cc.worldmandia.web;

import cc.worldmandia.url.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cc.worldmandia.web.WebConstants.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("/url-shortener")
public class UrlController {
    private final UrlServiceImpl urlService;
    private final String redirectToList = "redirect:/url-shortener/list";
    private String timeUrl;

    @GetMapping
    public String start() {
        return "main";
    }

    @PostMapping("/reklama")
    public String getAd(@RequestParam ("shortUrl") String shortUrl) {
        timeUrl = shortUrl;
        System.out.println(timeUrl);
        return "ad";
    }

    @PostMapping("/goToUrl")
    public String go() {
        Url url = urlService.findURLWithUsersByShortURL(timeUrl);
        System.out.println(url.getFullUrl());
        if(url.isEnabled())

        if (url != null) {
            urlService.incrementClickCount(url.getId());
            return "redirect:" + url.getFullUrl();
        }
            return "error-page";
    }

    @GetMapping("/list")
    public String getUrlList(Model model) {
        List<Url> urlList = urlService.findAll();
        model.addAttribute("urls", urlList);
        return "stats";
    }

    @GetMapping("/create")
    public String createUrlPage(Model model) {
        model.addAttribute("createUrl", new Url());
        return "newUrl";
    }

    @PostMapping("/create")
    public String createShortUrl(@RequestParam String fullUrl,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @ModelAttribute Url newUrl, Model model) {
        if(fullUrl == null || fullUrl.isEmpty()||fullUrl.length()> MAX_LENGTH) {
            model.addAttribute("errorFullUrl", INVALID_URL);
            return "newUrl";
        }
        if(title == null || title.isEmpty() || title.length()>MAX_LENGTH){
            model.addAttribute("errorTitle", INVALID_TITLE);
            return "newUrl";
        }
        if(description.length()>MAX_LENGTH){
            model.addAttribute("errorDescription", INVALID_DESCRIPTION);
            return "newUrl";
        }
        urlService.createUrl(newUrl);
        return redirectToList;
    }

    @GetMapping("/edit")
    public String editUrl(@RequestParam long id, Model model) {
        Url searchUrl = urlService.findById(id);
        model.addAttribute("url", searchUrl);
        return "editUrl";
    }

    @PostMapping("/edit")
    public String editUrl(@RequestParam String fullUrl,
                          @RequestParam String title,
                          @RequestParam String description,
                          @ModelAttribute Url editUrl, Model model) {
        if(fullUrl == null || fullUrl.isEmpty()||fullUrl.length()> MAX_LENGTH) {
            model.addAttribute("errorFullUrl", INVALID_URL);
            return "editUrl";
        }
        if(title == null || title.isEmpty() || title.length()>MAX_LENGTH){
            model.addAttribute("errorTitle", INVALID_TITLE);
            return "editUrl";
        }
        if(description.length()>MAX_LENGTH){
            model.addAttribute("errorDescription", INVALID_DESCRIPTION);
            return "editUrl";
        }
        urlService.updateTitleOrDescription(editUrl);
        return redirectToList;
    }

    @PostMapping("/delete")
    public String deleteUrl(@RequestParam long id) {
        urlService.deleteById(id);
        return redirectToList;
    }

    @PostMapping("/checked")
    public String updateEnabledStatus(@ModelAttribute Url url) {
        urlService.updateEnabledStatus(url);
        return redirectToList;
    }
}
