package ntnu.idi.project4.model;

//TODO: Change class to right data
public class Calculation {
  private int id;
  private int userId;
  private String expression;
  private String result;

  public Calculation() {
  }

  public Calculation(int id, int userId, String expression, String result) {
    this.id = id;
    this.userId = userId;
    this.expression = expression;
    this.result = result;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public String getExpression() {
    return expression;
  }

  public String getResult() {
    return result;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public void setResult(String result) {
    this.result = result;
  }
}

