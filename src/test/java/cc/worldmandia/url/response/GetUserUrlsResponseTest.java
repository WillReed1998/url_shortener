package cc.worldmandia.url.response;

import cc.worldmandia.url.Url;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetUserUrlsResponseTest {

    @Test
    void testSuccess() {
        List<Url> userUrls = new ArrayList<>();

        GetUserUrlsResponse getUserUrlsResponse = GetUserUrlsResponse.success(userUrls);

        assertEquals(GetUserUrlsResponse.Error.ok, getUserUrlsResponse.getError());
        assertEquals(userUrls, getUserUrlsResponse.getUserUrls());
    }

    @Test
    void testFailed() {
        GetUserUrlsResponse getUserUrlsResponse = GetUserUrlsResponse.failed();

        assertEquals(GetUserUrlsResponse.Error.failed, getUserUrlsResponse.getError());
        assertNull(getUserUrlsResponse.getUserUrls());
    }
}