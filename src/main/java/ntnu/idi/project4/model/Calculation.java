package ntnu.idi.project4.model;

import java.sql.Time;
import java.sql.Timestamp;

//TODO: Change class to right data
public class Calculation {
  private int id;
  private int userId;
  private String expression;
  private double result;
  private Timestamp timestamp;

  public Calculation() {
  }

  public Calculation(int id, int userId, String expression, double result, Timestamp timestamp) {
    this.id = id;
    this.userId = userId;
    this.expression = expression;
    this.result = result;
    this.timestamp = timestamp;
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

  public double getResult() {
    return result;
  }

  public Timestamp getTimestamp() {
    return timestamp;
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

  public void setResult(double result) {
    this.result = result;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}

