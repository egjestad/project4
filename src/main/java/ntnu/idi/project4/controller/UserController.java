package ntnu.idi.project4.controller;


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
  public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
    Optional<User> existingUser = userService.findUserByUsername(username);
    if (existingUser.isPresent()) {
      logger.info("User already exists");
      return ResponseEntity.badRequest().body("User already exists");
    }
    userService.registerUser(username, password);
    logger.info("User registered");
    return ResponseEntity.ok("User registered");
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
    Optional<User> user = userService.findUserByUsername(username);
    if (user.isPresent() && user.get().getPassword().equals(password)) {
      logger.info("User logged in");
      return ResponseEntity.ok("User logged in");
    }
    logger.info("Username or password incorrect");
    return ResponseEntity.status(401).body("Username or password incorrect");
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<User> getUserById(@PathVariable int id) {
    Optional<User> user = userService.findUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable int id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("User deleted");
  }
}
