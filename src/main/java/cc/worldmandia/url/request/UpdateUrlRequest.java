package cc.worldmandia.url.request;

import lombok.Data;

@Data
public class UpdateUrlRequest {

    private Long id;
    private String fullUrl;
    private final String shortUrl;
    private String title;
    private String description;
}
