package ntnu.idi.project4.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

@Component
public class TokenUtil {
  private final String SECRET_KEY = "yourSecretKeyHereYourSecretKeyHere";
  private final Duration ACCESS_TOKEN_VALIDITY = Duration.ofMinutes(5);
  private final Duration REFRESH_TOKEN_VALIDITY = Duration.ofDays(1);
  private final Logger logger = Logger.getLogger(TokenUtil.class.getName());

  public String generateAccessToken(int userId) {
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(SECRET_KEY);
    final JWTVerifier verifier = JWT.require(hmac512).build();
    return JWT.create()
            .withSubject(Integer.toString(userId))
            .withIssuer("project4")
            .withIssuedAt(now)
            .withExpiresAt(now.plusMillis(ACCESS_TOKEN_VALIDITY.toMillis()))
            .sign(hmac512);
  }

  public String getSECRET_KEY() {
    return SECRET_KEY;
  }

  public String verifyToken(String token) {
    try {
      final Algorithm hmac512 = Algorithm.HMAC512(SECRET_KEY);
      final JWTVerifier verifier = JWT.require(hmac512).build();
      DecodedJWT decodedJWT = verifier.verify(token);
      return decodedJWT.getSubject(); // Return userId if valid
    } catch (JWTVerificationException e) {
      return null; // Return null if invalid
    }
  }

  public int extractUserId(String token) {
    try {
      token = token.replace("Bearer ", "");
      logger.info("Extracting userId from token: " + token);
      String userIdStr = verifyToken(token);
      if (userIdStr != null) {
        logger.info("Extracted userId: " + userIdStr);
        return Integer.parseInt(userIdStr);
      } else {
        return -1;
      }
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Invalid userId in token");
    }
  }

  public String generateRefreshToken(int userId) {
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(SECRET_KEY);
    final JWTVerifier verifier = JWT.require(hmac512).build();
    return JWT.create()
            .withSubject(Integer.toString(userId))
            .withIssuer("project4")
            .withIssuedAt(now)
            .withExpiresAt(now.plusMillis(ACCESS_TOKEN_VALIDITY.toMillis()))
            .sign(hmac512);
  }
}
