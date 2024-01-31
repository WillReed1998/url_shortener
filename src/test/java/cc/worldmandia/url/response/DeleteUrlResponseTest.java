package cc.worldmandia.url.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DeleteUrlResponseTest {

    @Test
    void testSuccess() {
        DeleteUrlResponse result = DeleteUrlResponse.success();

        assertThat(result.getError()).isEqualTo(DeleteUrlResponse.Error.ok);
    }

    @Test
    void testFailed() {
        DeleteUrlResponse.Error error = DeleteUrlResponse.Error.invalidUrlId;

        DeleteUrlResponse result = DeleteUrlResponse.failed(error);

        assertThat(result.getError()).isEqualTo(error);
    }
}