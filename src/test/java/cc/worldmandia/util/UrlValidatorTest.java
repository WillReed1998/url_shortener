package cc.worldmandia.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UrlValidatorTest {

    @Test
    public void testValidHttpUrl() {
        assertTrue(UrlValidator.validUrl("http://www.example.com"));
    }

    @Test
    public void testValidHttpsUrl() {
        assertTrue(UrlValidator.validUrl("https://www.example.com"));
    }

    @Test
    public void testValidFtpUrl() {
        assertTrue(UrlValidator.validUrl("ftp://ftp.example.com"));
    }

    @Test
    public void testInvalidUrl() {
        assertFalse(UrlValidator.validUrl("not_a_valid_url"));
    }

    @Test
    public void testNullUrl() {
        assertFalse(UrlValidator.validUrl(null));
    }

    @Test
    public void testEmptyUrl() {
        assertFalse(UrlValidator.validUrl(""));
    }

    @Test
    public void testUrlWithSpecialCharacters() {
        assertTrue(UrlValidator.validUrl("https://www.example.com/<special>"));
    }

    @Test
    public void testUrlWithQueryParameters() {
        assertTrue(UrlValidator.validUrl("https://www.example.com/path?param1=value1&param2=value2"));
    }
}
