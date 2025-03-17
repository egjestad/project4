package ntnu.idi.project4.service;

import ntnu.idi.project4.exeptions.InncorectPasswordExeption;
import ntnu.idi.project4.exeptions.UserNotFoundExeption;
import ntnu.idi.project4.model.User;
import ntnu.idi.project4.repo.UserRepository;
import ntnu.idi.project4.security.TokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final TokenUtil tokenUtil;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public UserService(UserRepository userRepository, TokenUtil tokenUtil) {
    this.userRepository = userRepository;
    this.tokenUtil = tokenUtil;
  }

  public String authenticateUser(String username, String password) {
    logger.info("Authenticating user: " + username);
    User user = userRepository.findByUsername(username);
    logger.info("Found user: " + user);
    if (user != null) {
      logger.info("User password: " + user.getPassword());
      if (user.getPassword().equals(password)) {
        logger.info("User authenticated: " + user);
        return tokenUtil.generateToken(user.getId());
      } else {
        logger.info("Password incorrect");
        throw new InncorectPasswordExeption("Password incorrect");
      }
    } else {
      logger.info("User not found");
      throw new UserNotFoundExeption("User not found");
    }
  }

  public void registerUser(String username, String password) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    userRepository.save(user);
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

  public void updateUserPassword(int id, String password) {
    userRepository.updatePassword(id, password);
  }

  public void updateUserUsername(int id, String username) {
    userRepository.updateUsername(id, username);
  }

}
