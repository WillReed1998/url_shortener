package cc.worldmandia.url.response;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUrlResponseTest {

    @Test
    void testSuccess() {
        Long createUrlId = 123L;

        CreateUrlResponse result = CreateUrlResponse.success(createUrlId);

        assertThat(result.getError()).isEqualTo(CreateUrlResponse.Error.ok);
        assertThat(result.getCreateUrlId()).isEqualTo(createUrlId);
    }

    @Test
    void testFailed() {
        CreateUrlResponse.Error error = CreateUrlResponse.Error.invalidTitle;

        CreateUrlResponse result = CreateUrlResponse.failed(error);

        assertThat(result.getError()).isEqualTo(error);
        assertThat(result.getCreateUrlId()).isEqualTo(-1L);
    }
}