package ntnu.idi.project4.dto;

public class UserResponse {
  private int userId;
  private String username;

  public UserResponse() {
  }

  public UserResponse(String username) {
    this.username = username;
  }

  public UserResponse(int userId, String username) {
    this.username = username;
    this.userId = userId;
  }

  public UserResponse(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
