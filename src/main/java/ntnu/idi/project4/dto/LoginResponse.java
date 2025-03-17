package ntnu.idi.project4.dto;

public class LoginResponse {
  private String token;

  public LoginResponse() {
  }

  public LoginResponse(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
