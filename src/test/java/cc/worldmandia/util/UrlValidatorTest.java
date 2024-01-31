package cc.worldmandia.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testUrlWithPortNumber() {
        assertTrue(UrlValidator.validUrl("https://www.example.com:8080"));
    }

    @Test
    public void testUrlWithIPv4Address() {
        assertFalse(UrlValidator.validUrl("http://192.168.0.1"));
    }

    @Test
    public void testUrlWithIPv6Address() {
        assertFalse(UrlValidator.validUrl("http://[2001:db8::1]:8080"));
    }

    @Test
    public void testUrlWithPath() {
        assertTrue(UrlValidator.validUrl("https://www.example.com/path/to/resource"));
    }

    @Test
    public void testUrlWithFragment() {
        assertTrue(UrlValidator.validUrl("https://www.example.com#section"));
    }

    @Test
    public void testUrlWithUsernamePassword() {
        assertFalse(UrlValidator.validUrl("ftp://user:password@example.com"));
    }

    @Test
    public void testUrlWithTrailingSlash() {
        assertTrue(UrlValidator.validUrl("https://www.example.com/"));
    }
}