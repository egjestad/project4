package ntnu.idi.project4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ntnu.idi.project4.security.TokenUtil;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {
  private final TokenUtil tokenUtil = new TokenUtil();

  @Test
  @DisplayName("Test generateAccessToken")
  void generateTokenAndVerify() {
    String token = tokenUtil.generateAccessToken(1);
    System.out.println(token);
    assertNotNull(token);

    String userId = tokenUtil.verifyToken(token);
    assertEquals("1", userId);
  }

  @Test
  @DisplayName("Test verifyToken with invalid token")
  void verifyInvalidToken() {
    String invalidToken = "invalidToken123";
    assertNull(tokenUtil.verifyToken(invalidToken));
  }
}

