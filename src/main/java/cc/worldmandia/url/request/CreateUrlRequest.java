package cc.worldmandia.url.request;

import lombok.Data;

@Data
public class CreateUrlRequest {

    private String fullUrl;
    private String title;
    private String description;
}
