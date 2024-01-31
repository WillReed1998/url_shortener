package cc.worldmandia.util.UrlShortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "short-url.allowedCharacters=abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
        "short-url.keyLength=8"
})

public class ShortUrlConfigTest {

    @Autowired
    private ShortUrlConfig shortUrlConfig;

    @Test
    public void testShortUrlProperties() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", shortUrlConfig.getAllowedCharacters());
        assertEquals(8, shortUrlConfig.getKeyLength());
    }
}
