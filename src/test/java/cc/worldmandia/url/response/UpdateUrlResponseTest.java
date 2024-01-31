package cc.worldmandia.url.response;

import cc.worldmandia.url.Url;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUrlResponseTest {

    @Test
    void testSuccess() {
        Url updateUrl = new Url();

        UpdateUrlResponse updateUrlResponse = UpdateUrlResponse.success(updateUrl);

        assertEquals(UpdateUrlResponse.Error.ok, updateUrlResponse.getError());
        assertEquals(updateUrl, updateUrlResponse.getUpdateUrl());
    }

    @Test
    void testFailed() {
        UpdateUrlResponse.Error error = UpdateUrlResponse.Error.invalidUrlId;

        UpdateUrlResponse updateUrlResponse = UpdateUrlResponse.failed(error);

        assertEquals(error, updateUrlResponse.getError());
        assertNull(updateUrlResponse.getUpdateUrl());
    }
}