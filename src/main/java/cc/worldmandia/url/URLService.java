package cc.worldmandia.url;

import java.util.List;

public interface URLService {

    URL save(URL url);

    URL findById(Long id);

    List<URL> findAll();

    void deleteById(Long id);

}
