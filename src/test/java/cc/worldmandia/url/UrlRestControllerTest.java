package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.EnableUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.CreateUrlResponse;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import cc.worldmandia.user.UserServiceImpl;
import cc.worldmandia.util.UrlShortener.ShortUrlConfig;
import cc.worldmandia.util.UrlShortener.ShortUrlUtil;
import cc.worldmandia.web.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(controllers = UrlRestController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class UrlRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlRestService urlRestService;
    @MockBean
    private UrlServiceImpl urlService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserServiceImpl userService;

    @MockBean
    private UrlRestController controller;

    @Test
    void testCreate(){
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userService.save(user);

        Principal principal = () -> "someone@Gmail.com";

        ShortUrlUtil util = new ShortUrlUtil(new ShortUrlConfig());
        UrlRestService urlRestService1 = new UrlRestService(urlRepository, userService , userRepository, util);

        CreateUrlRequest request = new CreateUrlRequest();
        request.setFullUrl("https://www.baeldung.com/spring-boot-testing");
        request.setTitle("title");
        request.setDescription("description");

        Mockito.when(urlRestService1.create(user.getEmail(), request)).thenReturn(CreateUrlResponse.success(1L));

        assertAll(() -> controller.create(principal, request));
    }

    @Test
    void testGetUserUrls() throws Exception {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userRepository.save(user);

        Long userId = 3L;
        Url url = Url.builder().id(userId).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        String requestURI = "http://localhost:9999/api/v1/url_shortener/list";

        mockMvc.perform(get(requestURI)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetUserUrl() throws Exception {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userRepository.save(user);

        Long userId = 3L;
        Url url = Url.builder().id(userId).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        String requestURI = "http://localhost:9999/api/v1/url_shortener?id=" + userId;

        mockMvc.perform(get(requestURI)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testUpdateUrl() {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userService.save(user);

        Principal principal = () -> "someone@Gmail.com";

        Long userId = 3L;
        Url url = Url.builder()
                .id(userId)
                .fullUrl("https://www.baeldung.com/spring-boot-testing")
                .shortUrl("Rd5wCq")
                .enabled(true)
                .user(user)
                .title("title")
                .description("description")
                .build();
        urlRepository.save(url);

        UpdateUrlRequest request = new UpdateUrlRequest();
        request.setId(3L);
        request.setFullUrl("https://howtodoinjava.com/spring-boot2/testing/rest-controller-unit-test-example/");
        request.setTitle("title");
        request.setDescription("description");

        ShortUrlUtil util = new ShortUrlUtil(new ShortUrlConfig());
        UrlRestService urlRestService1 = new UrlRestService(urlRepository, userService , userRepository, util);

        assertAll(() -> controller.updateUrl(principal, request));
    }

    @Test
    void testDelete() throws Exception {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userRepository.save(user);

        Long userId = 3L;
        Url url = Url.builder().id(userId).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        String requestURI = "http://localhost:9999/api/v1/url_shortener?id=" + userId;

        mockMvc.perform(delete(requestURI)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGoToUrl() {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userService.save(user);

        Url url = Url.builder().id(3L).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        assertAll(() -> controller.redirect(3L));
    }

    @Test
    void testChangeEnable() {
        User user = User
                .builder()
                .id(2L)
                .email("someone@Gmail.com")
                .username("aqaqa")
                .build();
        userService.save(user);

        Principal principal = () -> "someone@Gmail.com";

        Url url = Url.builder().id(3L).fullUrl("https://www.baeldung.com/spring-boot-testing").shortUrl("Rd5wCq").user(user).build();
        urlRepository.save(url);

        EnableUrlRequest request = new EnableUrlRequest();
        request.setId(3L);
        request.setEnable(true);

        assertAll(() -> controller.changeEnable(principal, request));
    }
}