package ntnu.idi.project4.exeptions;

public class UserNotFoundExeption extends IllegalArgumentException {
  public UserNotFoundExeption(String message) {
    super(message);
  }

}
