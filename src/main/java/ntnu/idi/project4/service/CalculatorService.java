package ntnu.idi.project4.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

  /**
   * Calculate a result from two numbers with an operator
   */
  public double calc(double a, double b, String operator){
    if (operator == null) {
      throw new IllegalArgumentException("Operator cannot be null");
    } else if (operator.equals("+")) {
      return a + b;
    } else if (operator.equals("-")) {
      return a - b;
    } else if (operator.equals("x")) {
      return a * b;
    } else if (operator.equals("/")) {
      return a / b;
    } else {
      throw new IllegalArgumentException("Operator not supported");
    }
  }






}
