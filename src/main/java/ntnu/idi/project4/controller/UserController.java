package ntnu.idi.project4.controller;


import ntnu.idi.project4.dto.LoginResponse;
import ntnu.idi.project4.dto.UserRequest;
import ntnu.idi.project4.dto.UserResponse;
import ntnu.idi.project4.exeptions.InncorectPasswordExeption;
import ntnu.idi.project4.exeptions.UserNotFoundExeption;
import ntnu.idi.project4.model.User;
import ntnu.idi.project4.security.TokenUtil;
import ntnu.idi.project4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.logging.Logger;


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
    /*
    Optional<User> existingUser = userService.findUserByUsername(userRequest.getUsername());
    if (existingUser.isPresent()) {
      logger.info("User already exists");
      return ResponseEntity.badRequest().body("User already exists");
    } else {
    */

      userService.registerUser(userRequest.getUsername(), userRequest.getPassword());
      logger.info("User registered, logging in");
      LoginResponse loginResponse = userService.loginUser(userRequest.getUsername(), userRequest.getPassword());
      return ResponseEntity.ok().body(new LoginResponse(loginResponse.getToken()));
    //}


  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserRequest userRequest) {
    logger.info("Received login request");
    try {
      LoginResponse loginResponse = userService.loginUser(userRequest.getUsername(), userRequest.getPassword());
      String token = loginResponse.getToken();
      if (token != null) {
        logger.info("User authenticated, returning token: " + token);
        return ResponseEntity.ok().body(new LoginResponse(token));
      } else {
        logger.warning("Token is null");
      }
    } catch (UserNotFoundExeption e) {
      return ResponseEntity.status(401).body("User not found");
      //registerUser(userRequest);
    } catch (InncorectPasswordExeption e) {
      logger.info("Password incorrect");
      return ResponseEntity.status(401).body("Password incorrect");
    }
    logger.info("Username or password incorrect");
    return ResponseEntity.status(401).body("invalid username or password");
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

