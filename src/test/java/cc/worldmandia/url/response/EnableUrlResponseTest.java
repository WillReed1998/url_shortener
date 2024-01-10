package cc.worldmandia.url.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnableUrlResponseTest {

    @Test
    void testSuccess() {
        Long urlId = 1L;
        boolean enable = true;

        EnableUrlResponse enableUrlResponse = EnableUrlResponse.success(urlId, enable);

        assertEquals(EnableUrlResponse.Error.ok, enableUrlResponse.getError());
        assertEquals(urlId, enableUrlResponse.getId());
        assertEquals(enable, enableUrlResponse.isEnable());
    }

    @Test
    void testFailed() {
        EnableUrlResponse.Error error = EnableUrlResponse.Error.invalidUrlId;

        EnableUrlResponse enableUrlResponse = EnableUrlResponse.failed(error);

        assertEquals(error, enableUrlResponse.getError());
        assertEquals(-1L, enableUrlResponse.getId());
    }
}