package ntnu.idi.project4.controller;

import java.util.Optional;
import java.util.logging.Logger;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
  public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
    logger.info("received register request");

    if (userService.userExists(userRequest.getUsername())) {
      logger.warning("User already exists");
      return ResponseEntity.badRequest().body("User already exists");
    } else {
      userService.registerUser(userRequest.getUsername(), userRequest.getPassword());
      logger.info("User registered, logging in");
      return userService.getLoginResponse(userRequest.getUsername(), userRequest.getPassword());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserRequest userRequest) {
    return userService.getLoginResponse(userRequest.getUsername(), userRequest.getPassword());
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    int userId = tokenUtil.extractUserId(refreshToken);
    if (userId == -1) {
      return ResponseEntity.status(403).body("Invalid token");
    }
    String newAccessToken = tokenUtil.generateAccessToken(userId);
    return ResponseEntity.ok(newAccessToken);
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

