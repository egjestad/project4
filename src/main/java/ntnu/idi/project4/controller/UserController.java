package ntnu.idi.project4.controller;

import java.util.Optional;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletResponse;
import ntnu.idi.project4.dto.LoginResponse;
import ntnu.idi.project4.dto.UserRequest;
import ntnu.idi.project4.dto.UserResponse;
import ntnu.idi.project4.exeptions.InncorectPasswordExeption;
import ntnu.idi.project4.exeptions.UserNotFoundExeption;
import ntnu.idi.project4.model.User;
import ntnu.idi.project4.security.TokenUtil;
import ntnu.idi.project4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {
  private final UserService userService;
  private final Logger logger = Logger.getLogger(UserController.class.getName());
  private final TokenUtil tokenUtil = new TokenUtil();

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  //todo: move to service
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(
          @RequestBody UserRequest userRequest,
          HttpServletResponse response
  ) {
    logger.info("received register request");

    if (userService.userExists(userRequest.getUsername())) {
      logger.warning("User already exists");
      return ResponseEntity.badRequest().body("User already exists");
    } else {
      userService.registerUser(userRequest.getUsername(), userRequest.getPassword());
      logger.info("User registered, logging in");
      return userService.getLoginResponse(userRequest.getUsername(), userRequest.getPassword(), response);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(
          @RequestBody UserRequest userRequest,
          HttpServletResponse response
  ) {

    logger.info("Received login request for: " + userRequest.getUsername());

    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");

    return userService.getLoginResponse(userRequest.getUsername(), userRequest.getPassword(), response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(
          @CookieValue(value = "refreshToken", required = false) String refreshToken,
          HttpServletResponse response
  ) {
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");

    if (refreshToken == null|| refreshToken.isEmpty()) {
      return ResponseEntity.status(401).body("No refresh token found");
    }

    int userId = tokenUtil.extractUserId(refreshToken);
    if (userId == -1) {
      return ResponseEntity.status(401).body("Invalid refresh token");
    }

    User user = userService.findUserById(userId).orElseThrow(() -> new UserNotFoundExeption("User not found"));
    String accessToken = tokenUtil.generateAccessToken(user.getId());

    response.setHeader("Authorization", "Bearer " + accessToken);
    return ResponseEntity.ok().body(new LoginResponse(accessToken));
  }


  @GetMapping("/user/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
    Optional<User> user = userService.findUserById(id);
    if (user.isPresent()) {
      UserResponse userResponse = new UserResponse(user.get().getId(), user.get().getUsername());
      return ResponseEntity.ok(userResponse);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable int id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("User deleted");
  }
}

