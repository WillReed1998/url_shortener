package cc.worldmandia.web;

import cc.worldmandia.url.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        return "ad";
    }

    @PostMapping("/goToUrl")
    public String go() {
        //increment click counter
        return "redirect:" + timeUrl;
    }

    @GetMapping("/list")
    public String getUrlList(Model model) {
        List<Url> urlList = urlService.findAll();
        model.addAttribute("urls", urlList);
        return "stats";
    }

    @GetMapping("/create")
    public String createUrlPage(Model model) {
        model.addAttribute("notes", new Url());
        return "newUrl";
    }

    @PostMapping("/create")
    public String createShortUrl(@ModelAttribute Url newUrl) {
        urlService.save(newUrl);
        return redirectToList;
    }

    @GetMapping("/edit")
    public String editUrl(@RequestParam long id, Model model) {
        Url searchUrl = urlService.findById(id);
        model.addAttribute("url", searchUrl);
        return "editUrl";
    }

    @PostMapping("/edit")
    public String editUrl(@ModelAttribute Url newUrl) {
        urlService.save(newUrl);
        return redirectToList;
    }

    @PostMapping("/delete")
    public String deleteUrl(@RequestParam long id) {
        urlService.deleteById(id);
        return redirectToList;
    }

    @GetMapping("/{shortUrl}")
    public ModelAndView redirectToFullUrl(@PathVariable String shortUrl) {
        Url url = urlService.findURLWithUsersByShortURL(shortUrl);

        if (url != null) {
            urlService.incrementClickCount(url.getId());
            return new ModelAndView("redirect:" + url.getFullUrl());
        } else {
            return new ModelAndView("error-page");
        }
    }
}
