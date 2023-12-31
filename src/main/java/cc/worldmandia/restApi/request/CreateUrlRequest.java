package cc.worldmandia.restApi.request;

import lombok.Data;

@Data
public class CreateUrlRequest {

    private String fullUrl;
    private String shortUrl;
    private String title;
    private String description;

}
