package cc.worldmandia.database.service.impl;


import cc.worldmandia.database.entity.URL;
import cc.worldmandia.database.repository.URLRepository;
import cc.worldmandia.database.service.URLService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class URLServiceImpl implements URLService {

    private final URLRepository urlRepository;

    @Override
    public URL save(URL url) {
        return urlRepository.save(url);
    }

    @Override
    public URL findById(Long id) {
        return urlRepository.findById(id).orElse(null);
    }

    @Override
    public List<URL> findAll() {
        return urlRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

}
