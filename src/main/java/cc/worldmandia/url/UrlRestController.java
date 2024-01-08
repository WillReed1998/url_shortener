package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.EnableUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.*;
import cc.worldmandia.web.UrlServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Create a shortened URL",
            description = "Creates a shortened URL for the provided long URL. " +
                    "The endpoint requires authentication, and the newly created " +
                    "shortened URL will be associated with the logged-in user."
    )
    @PostMapping
    public CreateUrlResponse create(Principal principal, @RequestBody CreateUrlRequest request) {
        return service.create(principal.getName(), request);
    }

    @Operation(
            summary = "Get user's list of shortened URLs",
            description = "Retrieves the list of shortened URLs associated with the currently " +
                    "logged-in user. Requires authentication."
    )
    @GetMapping("/list")
    public GetUserUrlsResponse getUserUrls(Principal principal) {
        return service.getUserUrls(principal.getName());
    }

    @Operation(
            summary = "Get user's specific shortened URL",
            description = "Retrieves a specific shortened URL associated with " +
                    "the logged-in user. Requires authentication."
    )
    @GetMapping
    public GetUserUrlResponse getUserUrl(Principal principal, @RequestParam(name = "id") Long id) {
        return service.getUserUrl(principal.getName(), id);
    }

    @Operation(
            summary = "Update details of a shortened URL",
            description = "Updates the details (e.g., description, expiration date) of a shortened " +
                    "URL associated with the logged-in user. Requires authentication."
    )
    @PatchMapping
    public UpdateUrlResponse updateUrl(Principal principal, @RequestBody UpdateUrlRequest request) {
        return service.update(principal.getName(), request);
    }

    @Operation(
            summary = "Delete a shortened URL",
            description = "Deletes a specific shortened URL associated with the logged-in user. " +
                    "Requires authentication."
    )
    @DeleteMapping
    public DeleteUrlResponse delete(Principal principal, @RequestParam(name = "id") String id) {
        return service.delete(principal.getName(), Long.valueOf(id));
    }

    @Operation(
            summary = "Redirect to original URL",
            description = "Redirects to the original (full) URL associated with a specific shortened URL. " +
                    "Increments the click count for the URL."
    )
    @PostMapping(value = "/goToUrl")
    public ResponseEntity<Void> redirect(@RequestParam(name = "id") Long id) {
        Url url = urlService.findById(id);
        urlService.incrementClickCount(url.getId());

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getFullUrl())).build();
    }

    @Operation(
            summary = "Enable or disable redirect to original URL",
            description = "Enables or disables the ability to redirect to the original URL " +
                    "for a specific shortened URL associated with the logged-in user. " +
                    "Requires authentication."
    )
    @PostMapping("/enable")
    public EnableUrlResponse changeEnable(Principal principal, @RequestBody EnableUrlRequest request) {
        return service.changeEnable(principal.getName(), request);
    }
}

