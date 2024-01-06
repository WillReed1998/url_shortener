package cc.worldmandia.util.UrlShortener;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ShortUrlUtil.class)
public class ShortUrlUtilTest {

    @MockBean
    private ShortUrlConfig config;

    @Test
    public void testGenerateUniqueKey() {
        when(config.getKeyLength()).thenReturn(8);
        when(config.getAllowedCharacters()).thenReturn("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        ShortUrlUtil shortUrlUtil = new ShortUrlUtil(config);
        String generatedKey = shortUrlUtil.generateUniqueKey();

        assertEquals(8, generatedKey.length());
    }
}