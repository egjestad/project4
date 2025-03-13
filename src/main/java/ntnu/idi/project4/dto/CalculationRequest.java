package ntnu.idi.project4.dto;

public class CalculationRequest {
  private String expression;
  private int UserId;

  public CalculationRequest() {
  }

  public CalculationRequest(String operation) {
    this.expression = operation;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public int getUserId() {
    return UserId;
  }

  public void setUserId(int UserId) {
    this.UserId = UserId;
  }

}
