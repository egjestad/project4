package ntnu.idi.project4.service;

import ntnu.idi.project4.model.User;
import ntnu.idi.project4.repo.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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
