package ntnu.idi.project4.exeptions;

public class InncorectPasswordExeption extends IllegalArgumentException{
  public InncorectPasswordExeption(String message) {
    super(message);
  }
}
