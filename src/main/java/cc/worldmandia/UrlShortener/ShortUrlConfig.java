package cc.worldmandia.UrlShortener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "short-url")
@Getter
@Setter
@ConfigurationPropertiesScan
public class ShortUrlConfig {
    private String allowedCharacters;
    private int keyLength;
}