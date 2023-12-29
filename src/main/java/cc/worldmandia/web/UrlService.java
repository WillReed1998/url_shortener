package cc.worldmandia.web;

import cc.worldmandia.url.Url;

import java.util.List;

public interface UrlService {

    Url save(Url url);

    Url findById(Long id);

    List<Url> findAll();

    void deleteById(Long id);

}
