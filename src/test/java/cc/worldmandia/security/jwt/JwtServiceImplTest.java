package cc.worldmandia.security.jwt;

import cc.worldmandia.security.auth.CustomUserDetails;
import cc.worldmandia.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceImplTest {
    @Value("${token.secret.key}")
    private String jwtSecretKey;
    @InjectMocks
    private JwtServiceImpl jwtService;
    @Mock
    private CustomUserDetails userDetails;
    private static final String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJleGFtcGxlOEBtYWlsLmNvbSIsImlhdCI6MTcwNDUzNzAyNH0" +
            ".UPQe_0m9mIKxpFdFzNCHByDmSJr8LUqsm-oQ3-aTyYw";
    private static final String TEST_INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGFtcGxlMUBnbWFpbC5jb20iLCJpYXQiOjE3MDQ0MDMzMTZ9." +
            "BBIC2stCkuRiL9u6DeyOjdgRWHHCrLLdv1_bJ89-MAM";
    private static final String TEST_PASSWORD = "Test33pass";
    private static final String TEST_USERNAME = "example8@mail.com";

    @BeforeEach
    public void init() {
        User user = new User();
        user.setEmail(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        userDetails = new CustomUserDetails(user);

        ReflectionTestUtils.setField(jwtService, "jwtSecretKey", jwtSecretKey);
    }

    @Test
    void testExtractUserName() {
        String extractedUserName = jwtService.extractUserName(TEST_VALID_TOKEN);
        assertEquals(TEST_USERNAME, extractedUserName);
    }

    @Test
    void testGenerateToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key1", "value1");
        extraClaims.put("key2", "value2");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertEquals(TEST_USERNAME, jwtService.extractUserName(token));
    }

    @Test
    void testIsTokenValid() {
        String validToken = jwtService.generateToken(new HashMap<>(), userDetails);

        assertTrue(jwtService.isTokenValid(validToken, userDetails));
    }

    @Test
    void isTokenNotValid() {

        assertFalse(jwtService.isTokenValid(TEST_INVALID_TOKEN, userDetails));
    }

}