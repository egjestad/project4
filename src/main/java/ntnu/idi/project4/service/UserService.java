package ntnu.idi.project4.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ntnu.idi.project4.dto.LoginResponse;
import ntnu.idi.project4.exeptions.InncorectPasswordExeption;
import ntnu.idi.project4.exeptions.UserNotFoundExeption;
import ntnu.idi.project4.model.User;
import ntnu.idi.project4.repo.UserRepository;
import ntnu.idi.project4.security.TokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final TokenUtil tokenUtil;
  private final BCryptPasswordEncoder passwordEncoder;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public UserService(UserRepository userRepository, TokenUtil tokenUtil) {
    this.userRepository = userRepository;
    this.tokenUtil = tokenUtil;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  public ResponseEntity<?> getLoginResponse(String username, String password, HttpServletResponse response) {
    try {
      LoginResponse loginResponse = loginUser(username, password);
      if (loginResponse.getAccessToken() == null) {
        return ResponseEntity.status(401).body("Token not Generated");
      }
      String refreshToken = tokenUtil.generateRefreshToken(tokenUtil.extractUserId(loginResponse.getAccessToken()));
      response.addCookie(getRefreshCookie(refreshToken));
      logger.info("Login successful: " + loginResponse.getAccessToken());
      return ResponseEntity.ok().body(loginResponse);
    } catch (UserNotFoundExeption e) {
      return ResponseEntity.status(401).body("User not found");
    } catch (InncorectPasswordExeption e) {
      return ResponseEntity.status(401).body("Password incorrect");
    }
  }

  public Cookie getRefreshCookie(String refreshToken) {
    try {
      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/refresh");
      cookie.setMaxAge(60 * 60 * 24); //1 day
      return cookie;
    } catch (Exception e) {
      logger.error("Error creating cookie");
      return null;
    }
  }

  public boolean authenticateUser(String username, String password, User user) {
    return username.equals(user.getUsername()) && passwordEncoder.matches(password, user.getPassword());
  }

  public LoginResponse loginUser(String username, String password) {
    if (!userExists(username)) {
      throw new UserNotFoundExeption("User not found");
    }

    User user = userRepository.findByUsername(username);

    if (!authenticateUser(username, password, user)) {
      throw new InncorectPasswordExeption("Incorrect password");
    }

    return generateTokens(user.getId());
  }

  public LoginResponse generateTokens(int userId) {
    String accessToken = tokenUtil.generateAccessToken(userId);
    return new LoginResponse(accessToken);
  }

  public void registerUser(String username, String password) {
    User user = new User();

    user.setUsername(username);
    setHashedPassword(password, user);

    userRepository.save(user);
  }

  public boolean userExists(String username) {
    Optional<User> existingUser = findUserByUsername(username);
    return existingUser.isPresent();
  }

  public void setHashedPassword(String password, User user) {
    String hashedPassword = passwordEncoder.encode(password);
    user.setPassword(hashedPassword);
  }

  public Optional<User> findUserByUsername(String username) {
    return Optional.ofNullable(userRepository.findByUsername(username));
  }

  public Optional<User> findUserById(int id) {
    return Optional.ofNullable(userRepository.findById(id));
  }

  public void deleteUserById(int id) {
    userRepository.deleteById(id);
  }

}
