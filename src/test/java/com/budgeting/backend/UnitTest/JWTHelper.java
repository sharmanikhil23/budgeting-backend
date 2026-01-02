package com.budgeting.backend.UnitTest;
import com.budgeting.backend.global.JWT;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=0123456789abcdef0123456789abcdef",
        "jwt.access-token-expiration=2000",
        "jwt.refresh-token-expiration=5000"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JWTHelper {

    @Autowired
    private JWT jwt;

    private String accessToken;
    private String refreshToken;
    private String email;

    @BeforeAll
    void fixtureSetup(){
        email = "test@test.ca";
        Map<String, String> tokens = jwt.generateTokens(email);
        accessToken = tokens.get("accessToken");
        refreshToken = tokens.get("refreshToken");
    }

    @Test
    public void extractEmailfromAccessToken(){
        assertTrue(jwt.extractEmail(accessToken).equalsIgnoreCase(email),"Email must be same");
    }

    @Test
    public void extractEmailfromRefreshToken() {
        assertTrue(jwt.extractEmail(accessToken).equalsIgnoreCase(email), "Email must be same");
    }

    @Test
    public void checkTokensExpireAfter() throws InterruptedException {
        Thread.sleep(3000);
        // access token should be expired by now let's check if it throws exception
        assertThrows(ExpiredJwtException.class, ()->jwt.extractEmail(accessToken),"Access Token must throw " +
                "exception");
        
        Thread.sleep(4000);
        //by now refresh token must be expired too
        assertThrows(ExpiredJwtException.class, ()->jwt.extractEmail(refreshToken),"Refresh throw must throw " +
                "exception");
    }

}
