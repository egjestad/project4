package ntnu.idi.project4.repo;

import ntnu.idi.project4.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
/**
 * Repository class for managing User entities in the database.
 */
@Repository
public class UserRepository {

  private final JdbcTemplate jdbcTemplate;
  private final String TABLE_NAME = "users";

  private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getInt("id"));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    return user;
  };
/**
 * Constructs a new UserRepository with the given JdbcTemplate.
 *
 * @param jdbcTemplate the JdbcTemplate to use for database operations
 */
  public UserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Saves a new User to the database.
   *
   * @param user the User to save
   */
  public void save(User user) {
    String sql = "INSERT INTO " + TABLE_NAME + " (username, password) VALUES (?, ?)";
    jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
  }

  /**
   * Finds a User by their username.
   *
   * @param username the username of the User to find
   * @return the User with the given username
   */
  public User findByUsername(String username) {
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
    try {
      return jdbcTemplate.queryForObject(sql, userRowMapper, username);
    } catch (DataAccessException e) {
      return null;
    }
  }

  /**
   * Finds a User by their ID.
   *
   * @param id the ID of the User to find
   * @return the User with the given ID
   */
  public User findById(int id) {
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, userRowMapper, id);
  }

  /**
   * Deletes a User by their ID.
   *
   * @param id the ID of the User to delete
   */
  public void deleteById(int id) {
    String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * Deletes a User by their username.
   *
   * @param username the username of the User to delete
   */
  public void deleteByUsername(String username) {
    String sql = "DELETE FROM " + TABLE_NAME + " WHERE username = ?";
    jdbcTemplate.update(sql, username);
  }

  /**
   * Updates the username of a User.
   *
   * @param id the ID of the User to update
   * @param username the new username
   */
  public void updateUsername(int id, String username) {
    String sql = "UPDATE " + TABLE_NAME + " SET username = ? WHERE id = ?";
    jdbcTemplate.update(sql, username, id);
  }

  /**
   * Updates the password of a User.
   *
   * @param id the ID of the User to update
   * @param password the new password
   */
  public void updatePassword(int id, String password) {
    String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE id = ?";
    jdbcTemplate.update(sql, password, id);
  }
}
