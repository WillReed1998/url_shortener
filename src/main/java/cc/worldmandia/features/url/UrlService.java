package cc.worldmandia.features.url;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public void add(UrlDto urlDto){
        Url url = new Url();

        url.setTitle(urlDto.getTitle());
        url.setDescription(urlDto.getDescription());
        url.setFullUrl(urlDto.getFullUrl());
        url.setShortUrl(urlDto.getShortUrl());
        url.setCreatedAt(urlDto.getCreatedAt());
        url.setEndAt(urlDto.getEndAt());
        url.setClickCount(urlDto.getClickCount());

        urlRepository.save(url);
    }

    public List<UrlDto> getList(){
        return UrlDto.from(urlRepository.findAll());
    }

    public void delete(Long id){
        urlRepository.deleteById(id);
    }

    public void update(UrlDto urlDto){
        Url url = urlRepository.findById(urlDto.getId()).orElseThrow();

        url.setTitle(urlDto.getTitle());
        url.setDescription(urlDto.getDescription());
        url.setFullUrl(urlDto.getFullUrl());
        url.setShortUrl(urlDto.getShortUrl());
        url.setCreatedAt(urlDto.getCreatedAt());
        url.setEndAt(urlDto.getEndAt());
        url.setClickCount(urlDto.getClickCount());

        urlRepository.save(url);
    }

}