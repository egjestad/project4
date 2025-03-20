package ntnu.idi.project4.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;

public class JWTAuthFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final Logger logger = Logger.getLogger(JWTAuthFilter.class.getName());

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    public JWTAuthFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);



        if (authHeader == null|| !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // check Bearer auth header

        String accessToken  = authHeader.substring(7);
        if (accessToken.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        // if Bearer auth header exists, validate token, and extract userId from token.
        // Note that we have added userId as subject to the token when it is generated
        // Note also that the token comes in this format 'Bearer token'

        String userId = validateTokenAndGetUserId(accessToken);

        if (userId  == null) {
            logger.warning("Token expired or invalid");

            String refreshToken = getRefreshTokenFromCookies(request);
            if(refreshToken != null) {
                logger.info("Found refresh token in cookies");
                String newAccessToken = tokenUtil.refreshAccessToken(refreshToken);

                if (newAccessToken != null) {
                    logger.info("Refreshed access token");

                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    response.setStatus(HttpServletResponse.SC_OK);

                    userId = validateTokenAndGetUserId(newAccessToken);
                } else {
                    logger.warning("Refresh token expired or invalid, please login again");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Refresh token expired or invalid");
                    return;
                }
            } else {
                logger.warning("No refresh token found, please login again");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired or invalid");
                filterChain.doFilter(request, response);
            }
        }

        // if token is valid, add user details to the authentication context
        // Note that user details should be fetched from the database in real scenarios
        // this is case we will retrieve use details from mock
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // then, continue with authenticated user context
        filterChain.doFilter(request, response);
    }


    public String validateTokenAndGetUserId (String token) {
        if(token == null||token.trim().isEmpty()) {
            logger.warning("Token is null or empty");
            return null;
        }
        try {
            final Algorithm hmac512 = Algorithm.HMAC512(tokenUtil.getSECRET_KEY());;
            final JWTVerifier verifier = JWT.require(hmac512).build();
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            logger.warning("Token verification failed: " + verificationEx.getMessage());
            return null;
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        logger.info("Getting refresh token from cookies");
        if (request.getCookies() != null) {
            logger.info("Cookies found");
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    logger.info("Refresh token found in cookies");
                    return cookie.getValue();
                }
            }
        }
        logger.info("No refresh token found in cookies");
        return null;
    }
}
