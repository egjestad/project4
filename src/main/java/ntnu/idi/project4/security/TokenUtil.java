package ntnu.idi.project4.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TokenUtil {
  private final String SECRET_KEY = "yourSecretKeyHereYourSecretKeyHere";
  private final Duration TOKEN_VALIDITY = Duration.ofMinutes(5);

  public String generateToken(int userId) {
    final Instant now = Instant.now();
    final Algorithm hmac512 = Algorithm.HMAC512(SECRET_KEY);
    final JWTVerifier verifier = JWT.require(hmac512).build();
    return JWT.create()
            .withSubject(Integer.toString(userId))
            .withIssuer("project4")
            .withIssuedAt(now)
            .withExpiresAt(now.plusMillis(TOKEN_VALIDITY.toMillis()))
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
      return decodedJWT.getSubject(); // Return username if valid
    } catch (JWTVerificationException e) {
      return null; // Return null if invalid
    }
  }
}
