package cc.worldmandia.features.url;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {

    private Long id;
    private String title;
    private String description;
    private String fullUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime endAt;
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


    private static LocalDateTime $default$createdAt() {
        return LocalDateTime.now();
    }

    private static LocalDateTime $default$endAt() {
        return LocalDateTime.now().plusDays(30);
    }

    public static UrlDtoBuilder builder() {
        return new UrlDtoBuilder();
    }

    public static class UrlDtoBuilder {
        private Long id;
        private String title;
        private String description;
        private String fullUrl;
        private String shortUrl;
        private LocalDateTime createdAt$value;
        private boolean createdAt$set;
        private LocalDateTime endAt$value;
        private boolean endAt$set;
        private int clickCount;

        UrlDtoBuilder() {
        }

        public UrlDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UrlDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public UrlDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UrlDtoBuilder fullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
            return this;
        }

        public UrlDtoBuilder shortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
            return this;
        }

        public UrlDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt$value = createdAt;
            this.createdAt$set = true;
            return this;
        }

        public UrlDtoBuilder endAt(LocalDateTime endAt) {
            this.endAt$value = endAt;
            this.endAt$set = true;
            return this;
        }

        public UrlDtoBuilder clickCount(int clickCount) {
            this.clickCount = clickCount;
            return this;
        }

        public UrlDto build() {
            LocalDateTime createdAt$value = this.createdAt$value;
            if (!this.createdAt$set) {
                createdAt$value = UrlDto.$default$createdAt();
            }
            LocalDateTime endAt$value = this.endAt$value;
            if (!this.endAt$set) {
                endAt$value = UrlDto.$default$endAt();
            }
            return new UrlDto(this.id, this.title, this.description, this.fullUrl, this.shortUrl, createdAt$value, endAt$value, this.clickCount);
        }

        public String toString() {
            return "UrlDto.UrlDtoBuilder(id=" + this.id + ", title=" + this.title + ", description=" + this.description + ", fullUrl=" + this.fullUrl + ", shortUrl=" + this.shortUrl + ", createdAt$value=" + this.createdAt$value + ", endAt$value=" + this.endAt$value + ", clickCount=" + this.clickCount + ")";
        }
    }
}

