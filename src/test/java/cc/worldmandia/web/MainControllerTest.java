package cc.worldmandia.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @InjectMocks
    private UrlServiceImpl urlServiceImpl;
    @Test
    void testStart() throws Exception {
        MainController mainController = new MainController(urlServiceImpl);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

        mockMvc.perform(get("/url-shortener-main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @Test
    void testGetAd() throws Exception {
        String shortUrl = "example";
        MainController mainController = new MainController(urlServiceImpl);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();

        mockMvc.perform(post("/url-shortener-main/ad")
                .param("shortUrl", shortUrl))
                .andExpect(status().isOk())
                .andExpect(view().name("ad.html"));
    }
}