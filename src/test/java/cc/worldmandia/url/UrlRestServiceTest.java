package cc.worldmandia.url;

import cc.worldmandia.USSpringApplication;
import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.EnableUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.CreateUrlResponse;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import cc.worldmandia.user.UserServiceImpl;
import cc.worldmandia.util.UrlShortener.ShortUrlConfig;
import cc.worldmandia.util.UrlShortener.ShortUrlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(classes= USSpringApplication.class)
class UrlRestServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @MockBean
    private UrlRestService urlRestService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Test
    void testCreate() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userService.save(user);

        ShortUrlUtil util = new ShortUrlUtil(new ShortUrlConfig());

        CreateUrlRequest request = new CreateUrlRequest();
        request.setFullUrl("https://www.baeldung.com/spring-boot-testing");
        request.setTitle("title");
        request.setDescription("description");

        Mockito.when(urlRestService.create("someone@Gmail.com", request))
                .thenReturn(CreateUrlResponse.success(1L));

        assertAll(() -> urlRestService.create(user.getEmail(), request));
    }

    @Test
    void testGetUserUrls() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userRepository.save(user);

        Long idUrl = 1L;
        Url url = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);
        Url url2 = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url2);

        assertAll(() -> urlRestService.getUserUrls(user.getEmail()));

    }


    @Test
    void testGetUserUrl() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userRepository.save(user);

        Long idUrl = 1L;
        Url url = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        assertAll(() -> urlRestService.getUserUrl(user.getEmail(), idUrl));
    }

    @Test
    void testChangeEnable() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userRepository.save(user);

        Long idUrl = 1L;
        Url url = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        EnableUrlRequest request = new EnableUrlRequest();
        request.setId(idUrl);
        request.setEnable(false);

        assertAll(() -> urlRestService.changeEnable(user.getEmail(), request));
    }


    @Test
    void testUpdate() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userRepository.save(user);

        Long idUrl = 1L;
        Url url = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        UpdateUrlRequest request = new UpdateUrlRequest();
        request.setTitle("title");
        request.setDescription("description");

        assertAll(() -> urlRestService.update("someone@Gmail.com", request));
    }


    @Test
    void testDelete() {
        User user = User.builder().id(2L).email("someone@Gmail.com").build();
        userRepository.save(user);

        Long idUrl = 1L;
        Url url = Url.builder().id(idUrl).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        assertAll(() -> urlRestService.delete(user.getEmail(), idUrl));
    }


}