package cc.worldmandia.features.url;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlDto {

    private Long id;
    private String title;
    private String description;
    private String fullUrl;
    private String shortUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime endAt = LocalDateTime.now().plusDays(30);
    private int clickCount;

    public static UrlDto from(Url url) {
        return UrlDto
                .builder()
                .id(url.getId())
                .title(url.getTitle())
                .description(url.getDescription())
                .fullUrl(url.getFullUrl())
                .shortUrl(url.getShortUrl())
                .createdAt(url.getCreatedAt())
                .endAt(url.getEndAt())
                .clickCount(url.getClickCount())
                .build();
    }

    public static List<UrlDto> from(Iterable<Url> urls) {
        List<UrlDto> result = new ArrayList<>();

        for (Url url : urls) {
            result.add(from(url));
        }

        return result;
    }


}

