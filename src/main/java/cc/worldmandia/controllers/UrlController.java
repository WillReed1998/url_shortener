package cc.worldmandia.controllers;

import cc.worldmandia.url.URL;
import cc.worldmandia.url.URLServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/url-shortener")
public class UrlController {
    private final URLServiceImpl urlService;
    private final String redirectToList = "redirect:/api/v1/url-shortener/list";
    private String timeUrl;

    @GetMapping
    public String start() {
        return "main";
    }

    @PostMapping("/reklama")
    public String getAd(@RequestParam ("shortUrl") String shortUrl) {
        timeUrl = shortUrl;
        System.out.println("timeUrl" + timeUrl);
        return "ad";
    }

    @PostMapping("/goToUrl")
    public String go() {
        System.out.println("timeUrl" + timeUrl);
        return "redirect:" + timeUrl;
    }

    @GetMapping("/list")
    public String getUrlList(Model model) {
        List<URL> urlList = urlService.findAll();
        model.addAttribute("urls", urlList);
        return "stats";
    }

    @GetMapping("/create")
    public String createUrlPage(Model model) {
        model.addAttribute("notes", new URL());
        return "newUrl";
    }

    @PostMapping("/create")
    public String createShortUrl(@ModelAttribute URL newUrl) {
        urlService.save(newUrl);
        return "redirect:/api/v1/url-shortener/list";
    }

    @GetMapping("/edit")
    public String editUrl(@RequestParam long id, Model model) {
        URL searchUrl = urlService.findById(id);
        model.addAttribute("url", searchUrl);
        return "editUrl";
    }

    @PostMapping("/edit")
    public String editUrl(@ModelAttribute URL newUrl) {
        urlService.save(newUrl);
        return redirectToList;
    }

    @PostMapping("/delete")
    public String deleteUrl(@RequestParam long id) {
        urlService.deleteById(id);
        return redirectToList;
    }
}
