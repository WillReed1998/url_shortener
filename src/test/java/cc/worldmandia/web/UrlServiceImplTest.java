package cc.worldmandia.web;

import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserServiceImpl;
import cc.worldmandia.util.UrlShortener.ShortUrlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ShortUrlUtil shortUrlUtil;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UrlServiceImpl urlService;
    @Mock
    private Url mockUrl;

    @Test
    public void testCreateUrl() {
        Url newUrl = new Url();
        String email = "test@example.com";

        when(shortUrlUtil.generateUniqueKey()).thenReturn("abc123");
        when(userService.findByEmail(email)).thenReturn(new User());

        urlService.createUrl(newUrl, email);

        assertEquals("http://url-shortener/abc123", newUrl.getShortUrl());
        assertTrue(newUrl.isEnabled());

        verify(urlRepository).save(newUrl);
    }

    @Test
    void testIsExists() {
        long validId = 1L;
        when(urlRepository.existsById(validId)).thenReturn(true);
        assertTrue(urlService.exists(validId));
    }

    @Test
    void testNotExists() {
        long invalidId = 0L;
        assertFalse(urlService.exists(invalidId));
    }

    @Test
    void testFindById() {
        Url url = new Url();
        Long validId = 1L;

        when(urlRepository.findById(validId)).thenReturn(Optional.of(url));

        Url foundUrl = urlService.findById(validId);
        assertNotNull(foundUrl);

        verify(urlRepository).existsById(validId);
    }

    @Test
    void testFindById_NotFound() {
//        Url url = new Url();
        long invalidId = 1L;

        when(urlRepository.findById(invalidId)).thenReturn(Optional.empty());

        Url foundUrl = urlService.findById(invalidId);
        assertNull(foundUrl);
        verify(urlRepository, times(1)).findById(invalidId);
    }

    @Test
    void testUpdateTitleOrDescription() {
        Url existingUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .title("Old Title")
                .description("Old Description")
                .enabled(true)
                .build();

        Url updatedUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .title("New Title")
                .description("New Description")
                .enabled(true)
                .build();

        when(urlRepository.existsById(existingUrl.getId())).thenReturn(true);
        when(urlRepository.findById(existingUrl.getId())).thenReturn(Optional.of(existingUrl));
        when(urlRepository.save(any(Url.class))).thenReturn(updatedUrl);

        Url result = urlService.updateTitleOrDescription(updatedUrl);

        assertNotNull(result);
        assertEquals(updatedUrl.getTitle(), result.getTitle());
        assertEquals(updatedUrl.getDescription(), result.getDescription());
        assertEquals(existingUrl.getId(), result.getId());

        verify(urlRepository).findById(existingUrl.getId());
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void testFindAll() {
        List<Url> expectedUrls = Arrays.asList(
                Url.builder().id(1L).fullUrl("http://example1.com").title("Title 1").description("Description 1").enabled(true).build(),
                Url.builder().id(2L).fullUrl("http://example2.com").title("Title 2").description("Description 2").enabled(true).build()
        );

        when(urlRepository.findAll()).thenReturn(expectedUrls);

        List<Url> resultUrls = urlService.findAll();

        assertNotNull(resultUrls);
        assertEquals(expectedUrls.size(), resultUrls.size());

        for (int i = 0; i < expectedUrls.size(); i++) {
            assertEquals(expectedUrls.get(i), resultUrls.get(i));
        }

        verify(urlRepository).findAll();
    }

    @Test
    void testFindAllByUser() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .password("password123")
                .token("token123")
                .urls(Collections.singletonList(mockUrl))
                .build();
        List<Url> expectedUrls = Arrays.asList(
                Url.builder().id(1L).fullUrl("http://example1.com").title("Title 1").description("Description 1").enabled(true).user(user).build(),
                Url.builder().id(2L).fullUrl("http://example2.com").title("Title 2").description("Description 2").enabled(true).user(user).build()
        );

        when(urlRepository.findAllByUser(user)).thenReturn(expectedUrls);

        List<Url> resultUrls = urlService.findAllByUser(user);

        assertNotNull(resultUrls);
        assertEquals(expectedUrls.size(), resultUrls.size());

        verify(urlRepository).findAllByUser(user);
    }

    @Test
    void testDeleteById() {
        Long idToDelete  = 1L;
        urlService.deleteById(idToDelete );
        verify(urlRepository).deleteById(idToDelete );
    }

    @Test
    void testFindURLWithUsersByShortURL() {
        String shortURL = "abc123";
        Url expectedUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .title("Example Title")
                .description("Example Description")
                .enabled(true)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlRepository.findURLWithUsersByShortURL(shortURL)).thenReturn(Optional.of(expectedUrl));

        Url resultUrl = urlService.findURLWithUsersByShortURL(shortURL);

        assertNotNull(resultUrl);
        assertEquals(expectedUrl, resultUrl);

        verify(urlRepository).findURLWithUsersByShortURL(shortURL);    }

    @Test
    void testIncrementClickCount() {
        Long id = 1L;

        urlService.incrementClickCount(id);

        verify(urlRepository).incrementClickCountById(id);
    }


    @Test
    void testUpdateEnabledStatus_ToDisabled() {
        Long id = 1L;
        Url existingUrl = Url.builder()
                .id(id)
                .fullUrl("http://example.com")
                .title("Example Title")
                .description("Example Description")
                .enabled(true)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(existingUrl));
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Url resultUrl = urlService.updateEnabledStatus(existingUrl);

        assertNotNull(resultUrl);
        assertEquals(id, resultUrl.getId());
        assertEquals(existingUrl.getFullUrl(), resultUrl.getFullUrl());
        assertEquals(existingUrl.getTitle(), resultUrl.getTitle());
        assertEquals(existingUrl.getDescription(), resultUrl.getDescription());

        verify(urlRepository).findById(id);
        verify(urlRepository).save(existingUrl);
    }

    @Test
    void testUpdateEnabledStatus_ToEnabled() {
        Long id = 1L;
        Url existingUrl = Url.builder()
                .id(id)
                .fullUrl("http://example.com")
                .title("Example Title")
                .description("Example Description")
                .enabled(false)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(existingUrl));
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Url resultUrl = urlService.updateEnabledStatus(existingUrl);

        assertNotNull(resultUrl);
        assertEquals(id, resultUrl.getId());
        assertEquals(existingUrl.getFullUrl(), resultUrl.getFullUrl());
        assertEquals(existingUrl.getTitle(), resultUrl.getTitle());
        assertEquals(existingUrl.getDescription(), resultUrl.getDescription());

        verify(urlRepository).findById(id);
        verify(urlRepository).save(existingUrl);
    }

    @Test
    void testProlongEndDate() {
        Long id = 1L;
        LocalDateTime originalEndDate = LocalDateTime.now().plusDays(5);
        Url existingUrl = Url.builder()
                .id(id)
                .fullUrl("http://example.com")
                .title("Example Title")
                .description("Example Description")
                .enabled(true)
                .endAt(originalEndDate)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(existingUrl));
        when(urlRepository.save(any(Url.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Url resultUrl = urlService.prolongEndDate(existingUrl);

        assertNotNull(resultUrl);
        assertEquals(id, resultUrl.getId());
        assertEquals(existingUrl.getFullUrl(), resultUrl.getFullUrl());
        assertEquals(existingUrl.getTitle(), resultUrl.getTitle());
        assertEquals(existingUrl.getDescription(), resultUrl.getDescription());
        assertTrue(resultUrl.getEndAt().isAfter(originalEndDate));

        verify(urlRepository).findById(id);
        verify(urlRepository).save(existingUrl);
    }

    @Test
    void testGetFullUrl_ValidAndEnabled() {
        // Arrange
        String shortUrl = "abc123";
        Url existingUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .title("Example Title")
                .description("Example Description")
                .enabled(true)
                .clickCount(2)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(existingUrl);
        doNothing().when(urlRepository).incrementClickCountById(existingUrl.getId());

        // Act
        RedirectView resultView = urlService.getFullUrl(shortUrl);

         //Assert
        assertNotNull(resultView);
        assertEquals(existingUrl.getFullUrl(), resultView.getUrl());
        assertEquals(2, existingUrl.getClickCount());

        // Verify that findByShortUrl and incrementClickCountById methods were called with the correct shortUrl and id
        verify(urlRepository).findByShortUrl(shortUrl);
        verify(urlRepository).incrementClickCountById(existingUrl.getId());
    }

    @Test
    void testGetFullUrl_InvalidOrDisabled() {
        // Arrange
        String shortUrl = "invalidShortUrl";
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(null);

        // Act
        RedirectView resultView = urlService.getFullUrl(shortUrl);

        // Assert
        assertNotNull(resultView);
        assertEquals("/error", resultView.getUrl());

        // Verify that findByShortUrl method was called with the correct shortUrl
        verify(urlRepository).findByShortUrl(shortUrl);
        // Verify that incrementClickCountById was not called (since the URL is invalid or disabled)
        verify(urlRepository, never()).incrementClickCountById(anyLong());
    }
}