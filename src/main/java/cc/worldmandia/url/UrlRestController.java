package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.EnableUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.*;
import cc.worldmandia.web.UrlServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;

@Tag(name = "Main methods")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/url_shortener")
public class UrlRestController {

    private final UrlRestService service;
    private final UrlServiceImpl urlService;

    @PostMapping
    public CreateUrlResponse create(Principal principal, @RequestBody CreateUrlRequest request){
        return service.create(principal.getName(), request);
    }

    @GetMapping("/list")
    public GetUserUrlsResponse getUserUrls(Principal principal){
        return  service.getUserUrls(principal.getName());
    }

    @GetMapping
    public GetUserUrlResponse getUserUrl(Principal principal, @RequestParam(name = "id") Long id){
        return service.getUserUrl(principal.getName(), id);
    }

    @PatchMapping
    public UpdateUrlResponse updateUrl(Principal principal, @RequestBody UpdateUrlRequest request){
        return service.update(principal.getName(), request);
    }

    @DeleteMapping
    public DeleteUrlResponse delete(Principal principal, @RequestParam(name = "id") String id){
        return service.delete(principal.getName(), Long.valueOf(id));
    }

//    @PostMapping("/goToUrl")
//    public String goToUrl(@RequestParam(name = "id") Long id){
//        Url url = urlService.findById(id);
//
//        if (url.isEnabled()){
//            urlService.incrementClickCount(url.getId());
//            return url.getFullUrl();
//        }
//
//        return String.valueOf(HttpStatus.NOT_FOUND);
//    }

//    @SneakyThrows
//    @PostMapping("/goToUrl")
//    public Response goToUrl(@RequestParam(name = "id") Long id){
//        Url url = urlService.findById(id);
//
//        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url.getFullUrl()).build();
//
//        return Response.temporaryRedirect(uriComponents.toUri()).build();
//    }

    @PostMapping(value = "/goToUrl")
    public ResponseEntity<Void> redirect(@RequestParam(name = "id") Long id){
        Url url = urlService.findById(id);
        urlService.incrementClickCount(url.getId());

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getFullUrl())).build();
    }

    @PostMapping("/enable")
    public EnableUrlResponse changeEnable(Principal principal, @RequestBody EnableUrlRequest request){
        return service.changeEnable(principal.getName(), request);
    }

    public String checked(){ return null;}
    //    @PostMapping("/checked")
    //    public String updateEnabledStatus(@ModelAttribute Url url) {
    //        urlService.updateEnabledStatus(url);
    //        return redirectToList;
    //    }


    public  String prolog(){ return null;}
    //    @PostMapping("/prolong")
    //    public String prolongEndDate(@ModelAttribute Url url) {
    //        urlService.prolongEndDate(url);
    //        return redirectToList;
    //    }
}
