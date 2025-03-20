package ntnu.idi.project4.dto;

public class LoginResponse {
  private String accessToken;

  public LoginResponse() {
  }

  public LoginResponse(String token) {
    this.accessToken = token;
  }


  public String getAccessToken() {
    return accessToken;
  }

}
