package cc.worldmandia.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieServiceTest {

    @Test
    void testCreateCookie() {
        CookieService cookieService = new CookieService();
        String cookieName = "testCookie";
        String value = "testValue";

        Cookie createdCookie = cookieService.createCookie(cookieName, value);

        assertNotNull(createdCookie);
        assertEquals(cookieName, createdCookie.getName());
        assertEquals(value, createdCookie.getValue());
        assertTrue(createdCookie.isHttpOnly());
        assertEquals(2 * 360 * 1000, createdCookie.getMaxAge());
        assertEquals("/", createdCookie.getPath());
    }

    @Test
    void testDeleteCookie() {
        CookieService cookieService = new CookieService();
        String cookieName = "testCookie";

        Cookie deletedCookie = cookieService.deleteCookie(cookieName);

        assertNotNull(deletedCookie);
        assertEquals(cookieName, deletedCookie.getName());
        assertEquals("", deletedCookie.getValue());
        assertTrue(deletedCookie.isHttpOnly());
        assertEquals(0, deletedCookie.getMaxAge());
        assertEquals("/", deletedCookie.getPath());
    }

    @Test
    void testGetCookie() {
        CookieService cookieService = new CookieService();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        Cookie[] cookies = {
                new Cookie("cookie1", "value1"),
                new Cookie("cookie2", "value2"),
                new Cookie("cookie3", "value3")
        };

        when(mockRequest.getCookies()).thenReturn(cookies);

        Cookie resultCookie = cookieService.getCookie(mockRequest, "cookie2");

        assertEquals("cookie2", resultCookie.getName());
        assertEquals("value2", resultCookie.getValue());
    }
}