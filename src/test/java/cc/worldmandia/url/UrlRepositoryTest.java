package cc.worldmandia.url;

import cc.worldmandia.USSpringApplication;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(classes= USSpringApplication.class)
class UrlRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindURLWithUsersByShortURLCorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        Optional<Url> found = urlRepository.findURLWithUsersByShortURL(url1.getShortUrl());
        assertThat(found.get().getShortUrl()).isEqualTo(url1.getShortUrl());
    }

    @Test
    void testFindURLWithUsersByShortURLIncorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        Optional<Url> found = urlRepository.findURLWithUsersByShortURL("Rd5wCq");
        assertThat(found.get().getShortUrl()).isNotEqualTo("Rd5wC6");
    }

    @Test
    void testFindByShortUrlCorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        Url found = urlRepository.findByShortUrl(url1.getShortUrl());
        assertThat(found.getShortUrl()).isEqualTo(url1.getShortUrl());
    }

    @Test
    void testFindByShortUrlIncorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        Url found = urlRepository.findByShortUrl(url1.getShortUrl());
        assertThat(found.getShortUrl()).isNotEqualTo("Rd5wC6");
    }

    @Test
    void testGetUserUrlsCorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url1);
        Url url2 = Url.builder().fullUrl("https://getbootstrap.com/docs/4.1/components/alerts/").shortUrl("nQ1hTv").user(user).build();
        urlRepository.save(url2);
        Url url3 = Url.builder().fullUrl("https://www.geeksforgeeks.org/classes-objects-java/").shortUrl("pL5Mfg").user(user).build();
        urlRepository.save(url3);

        List<Url> urls = urlRepository.getUserUrls(user.getId());

        assertThat(urls).hasSize(3).extracting(Url::getShortUrl).containsOnly(url1.getShortUrl(), url2.getShortUrl(), url3.getShortUrl());
        assertThat(urls.size()).isEqualTo(3);
        assertThat(urls.get(0)).isEqualTo(url1);
    }

    @Test
    void testGetUserUrlsIncorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url1);
        Url url2 = Url.builder().fullUrl("https://getbootstrap.com/docs/4.1/components/alerts/").shortUrl("nQ1hTv").user(user).build();
        urlRepository.save(url2);
        Url url3 = Url.builder().fullUrl("https://www.geeksforgeeks.org/classes-objects-java/").shortUrl("pL5Mfg").user(user).build();
        urlRepository.save(url3);

        List<Url> urls = urlRepository.getUserUrls(user.getId());

        assertThat(urls).hasSize(3).extracting(Url::getShortUrl).containsOnly(url1.getShortUrl(), url2.getShortUrl(), url3.getShortUrl());
        assertThat(urls.size()).isEqualTo(3);
        assertThat(urls.get(0)).isEqualTo(url1);
    }

    @Test
    void testIncrementClickCountByIdIncorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        UrlRepository repository = mock(UrlRepository.class);
        doNothing().when(repository).incrementClickCountById(url1.getId());

        assertThat(url1.getClickCount()).isNotEqualTo(1);
    }

    @Test
    void testIncrementClickCountByIdCorrect() {
        User user = User.builder().id(155L).email("aaa@gmail.com").username("aaa").password("Paaaaaaa1").build();
        userRepository.save(user);

        Url url1 = Url.builder().user(user).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").build();
        entityManager.persistAndFlush(url1);

        UrlRepository repository = mock(UrlRepository.class);
        doNothing().when(repository).incrementClickCountById(url1.getId());
        repository.incrementClickCountById(url1.getId());
        repository.incrementClickCountById(url1.getId());
        repository.incrementClickCountById(url1.getId());

        verify(repository, times(3)).incrementClickCountById(url1.getId());
    }

}