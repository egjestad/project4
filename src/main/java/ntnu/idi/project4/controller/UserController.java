package ntnu.idi.project4.controller;


import ntnu.idi.project4.dto.UserRequest;
import ntnu.idi.project4.dto.UserResponse;
import ntnu.idi.project4.model.User;
import ntnu.idi.project4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.logging.Logger;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
  private final UserService userService;
  private final Logger logger = Logger.getLogger(UserController.class.getName());

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public void registerUser(@RequestBody UserRequest userRequest) {
    Optional<User> existingUser = userService.findUserByUsername(userRequest.getUsername());
    if (existingUser.isPresent()) {
      logger.info("User already exists");
    }
    userService.registerUser(userRequest.getUsername(), userRequest.getPassword());
    logger.info("User registered, logging in");
    loginUser(userRequest);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserRequest userRequest) {
    Optional<User> user = userService.findUserByUsername(userRequest.getUsername());
    if (user.isPresent() && user.get().getPassword().equals(userRequest.getPassword())) {
      logger.info("User logged in");
      UserResponse userResponse = new UserResponse(user.get().getId(), user.get().getUsername());
      return ResponseEntity.ok(userResponse);
    } else if (!user.isEmpty()) {
      logger.info("User not found, registering user");
      registerUser(userRequest);
    }
    logger.info("Username or password incorrect");
    return ResponseEntity.status(401).body("Username or password incorrect");
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
