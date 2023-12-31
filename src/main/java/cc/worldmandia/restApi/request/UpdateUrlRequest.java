package cc.worldmandia.restApi.request;

import lombok.Data;

@Data
public class UpdateUrlRequest {

    private Long id;
    private String fullUrl;
    private String shortUrl;
    private String title;
    private String description;
}
