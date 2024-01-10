package cc.worldmandia.url.response;

import cc.worldmandia.url.Url;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetUserUrlResponseTest {

    @Test
    void testSuccess() {
        Url userUrl = new Url();

        GetUserUrlResponse getUserUrlResponse = GetUserUrlResponse.success(userUrl);

        assertEquals(GetUserUrlsResponse.Error.ok, getUserUrlResponse.getError());
        assertNotNull(getUserUrlResponse.getUserUrl());
    }

    @Test
    void testFailed() {
        GetUserUrlResponse getUserUrlResponse = GetUserUrlResponse.failed(GetUserUrlResponse.Error.failed);

       // assertEquals(GetUserUrlsResponse.Error.failed, getUserUrlResponse.getError());
        assertNull(getUserUrlResponse.getUserUrl());
    }
}