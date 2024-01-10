package cc.worldmandia.web;

import cc.worldmandia.url.Url;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private UrlServiceImpl urlService;
    @InjectMocks
    private UrlController urlController;
    private static final String INVALID_URL = "Invalid URL. Enter correct URL please.";

    @Test
    void testGetUrlList() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();

        Principal principal = () -> "test@example.com";

        User mockUser = User.builder().id(1L).email("test@example.com").username("testuser").build();
        when(userService.findByEmail(principal.getName())).thenReturn(mockUser);

        List<Url> mockUrlList = Collections.singletonList(
                Url.builder()
                        .id(1L)
                        .fullUrl("http://example.com")
                        .shortUrl("abc123")
                        .enabled(true)
                        .clickCount(2)
                        .user(mockUser)
                        .build()
        );
        when(urlService.findAllByUser(mockUser)).thenReturn(mockUrlList);

        mockMvc.perform(MockMvcRequestBuilders.get("/url-shortener/list").principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("stats"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "testuser"))
                .andExpect(model().attributeExists("urls"))
                .andExpect(model().attribute("urls", mockUrlList));
    }

    @Test
    void testCreateUrlPage() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();

        Principal principal = () -> "test@example.com";

        User mockUser = User.builder().id(1L).email("test@example.com").username("testuser").build();
        when(userService.findByEmail(principal.getName())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/url-shortener/create").principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("newUrl"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "testuser"))
                .andExpect(model().attributeExists("createUrl"));
    }

    @Test
    void testCreateShortUrl_Success() {
        Model model = new BindingAwareModelMap();
        Principal principal = () -> "test@example.com";
        Url newUrl = new Url();
        newUrl.setFullUrl("http://example.com");
        newUrl.setTitle("Example Title");
        newUrl.setDescription("Example Description");

        String result = urlController.createShortUrl(
                newUrl.getFullUrl(),
                newUrl.getTitle(),
                newUrl.getDescription(),
                newUrl,
                model,
                principal
        );

        assertEquals("redirect:/url-shortener/list", result);
        verify(urlService, times(1)).createUrl(newUrl, principal.getName());
        assertFalse(model.containsAttribute("errorFullUrl"));
        assertFalse(model.containsAttribute("errorTitle"));
        assertFalse(model.containsAttribute("errorDescription"));
    }

    @Test
    void testCreateShortUrl_InvalidFullUrl() {
        Model model = new BindingAwareModelMap();
        Principal principal = () -> "test@example.com";
        Url newUrl = new Url();
        newUrl.setFullUrl("invalidUrl");

        String result = urlController.createShortUrl(
                newUrl.getFullUrl(),
                newUrl.getTitle(),
                newUrl.getDescription(),
                newUrl,
                model,
                principal
        );

        assertEquals("newUrl", result);
        verify(urlService, never()).createUrl(any(), any());
        assertTrue(model.containsAttribute("errorFullUrl"));
        assertEquals(INVALID_URL, model.getAttribute("errorFullUrl"));
    }

    @Test
    void testEditUrl_Get() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        long urlId = 1L;
        Url mockUrl = Url.builder()
                .id(urlId)
                .fullUrl("http://example.com")
                .shortUrl("abc123")
                .enabled(true)
                .clickCount(2)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        when(urlService.findById(urlId)).thenReturn(mockUrl);

        mockMvc.perform(MockMvcRequestBuilders.get("/url-shortener/edit").param("id", String.valueOf(urlId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("editUrl"))
                .andExpect(model().attributeExists("url"))
                .andExpect(model().attribute("url", mockUrl));

        verify(urlService, times(1)).findById(urlId);
    }

    @Test
    void testEditUrl_Success() throws Exception {
        Model model = new BindingAwareModelMap();
        Url editUrl = new Url();
        editUrl.setFullUrl("http://example.com");
        editUrl.setTitle("Example Title");
        editUrl.setDescription("Example Description");

        String result = urlController.editUrl(
                editUrl.getFullUrl(),
                editUrl.getTitle(),
                editUrl.getDescription(),
                editUrl,
                model
        );

        assertEquals("redirect:/url-shortener/list", result);
        verify(urlService, times(1)).updateTitleOrDescription(editUrl);
        assertFalse(model.containsAttribute("errorFullUrl"));
        assertFalse(model.containsAttribute("errorTitle"));
        assertFalse(model.containsAttribute("errorDescription"));
    }

    @Test
    void testDeleteUrl() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();

        long urlId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.post("/url-shortener/delete").param("id", String.valueOf(urlId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/url-shortener/list"));

        verify(urlService, times(1)).deleteById(urlId);
    }

    @Test
    void testUpdateEnabledStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        Url mockUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .shortUrl("abc123")
                .enabled(true)
                .clickCount(2)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/url-shortener/checked")
                        .param("id", String.valueOf(mockUrl.getId()))
                        .param("fullUrl", mockUrl.getFullUrl())
                        .param("shortUrl", mockUrl.getShortUrl())
                        .param("enabled", String.valueOf(mockUrl.isEnabled()))
                        .param("clickCount", String.valueOf(mockUrl.getClickCount()))
                        .param("user.id", String.valueOf(mockUrl.getUser().getId()))
                        .param("user.email", mockUrl.getUser().getEmail()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/url-shortener/list"));

        verify(urlService).updateEnabledStatus(any());
    }

    @Test
    void testProlongEndDate() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        Url mockUrl = Url.builder()
                .id(1L)
                .fullUrl("http://example.com")
                .shortUrl("abc123")
                .enabled(true)
                .clickCount(2)
                .user(User.builder().id(1L).email("test@example.com").build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/url-shortener/prolong")
                        .param("id", String.valueOf(mockUrl.getId()))
                        .param("fullUrl", mockUrl.getFullUrl())
                        .param("shortUrl", mockUrl.getShortUrl())
                        .param("enabled", String.valueOf(mockUrl.isEnabled()))
                        .param("clickCount", String.valueOf(mockUrl.getClickCount()))
                        .param("user.id", String.valueOf(mockUrl.getUser().getId()))
                        .param("user.email", mockUrl.getUser().getEmail()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/url-shortener/list"));
    }

    @Test
    void testRedirectToFullUrl() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        String shortUrl = "abc123";
        String fullUrl = "http://example.com";

        when(urlService.getFullUrl(shortUrl)).thenReturn(new RedirectView(fullUrl));

        mockMvc.perform(MockMvcRequestBuilders.get("/url-shortener/{shortUrl}", shortUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(fullUrl));

        verify(urlService, times(1)).getFullUrl(shortUrl);
    }
}