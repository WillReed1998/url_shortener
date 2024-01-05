package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.CreateUrlResponse;
import cc.worldmandia.url.response.DeleteUrlResponse;
import cc.worldmandia.url.response.GetUserUrlsResponse;
import cc.worldmandia.url.response.UpdateUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/url_shortener")
public class UrlRestController {

    private final UrlRestService service;

    @PostMapping
    public CreateUrlResponse create(Principal principal, @RequestBody CreateUrlRequest request){
        return service.create(principal.getName(), request);
    }

    @GetMapping("/getUserUrls")
    public GetUserUrlsResponse getUserUrls(Principal principal){
        return  service.getUserUrls(principal.getName());
    }

    @PatchMapping
    public UpdateUrlResponse updateUrl(Principal principal, @RequestBody UpdateUrlRequest request){
        return service.update(principal.getName(), request);
    }

    @DeleteMapping
    public DeleteUrlResponse delete(Principal principal, @RequestParam(name = "id") Long id){
        return service.delete(principal.getName(), id);
    }

}
