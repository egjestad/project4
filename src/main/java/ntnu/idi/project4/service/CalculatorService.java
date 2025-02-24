package ntnu.idi.project4.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CalculatorService {

  public double calculate(String expression) {

    List<String> tokens = tokenize(expression);
    while (tokens.size() >= 3){
      // Multiplication and division have higher precedence than addition and subtraction
      if ((tokens.size() > 3) &&(tokens.get(1).equals("+") || tokens.get(1).equals("-")) && (tokens.get(3).equals("*") || tokens.get(3).equals("/"))) {
        double result = calc(Double.parseDouble(tokens.get(2)), Double.parseDouble(tokens.get(4)), tokens.get(3));
        tokens.remove(2);
        tokens.remove(2);
        tokens.remove(2);
        tokens.add(2, String.valueOf(result));

      } else {
        double result = calc(Double.parseDouble(tokens.get(0)), Double.parseDouble(tokens.get(2)), tokens.get(1));
        tokens.remove(0);
        tokens.remove(0);
        tokens.remove(0);
        tokens.add(0,String.valueOf(result));
      }
    }
    return Double.parseDouble(tokens.get(0));
  }

  private List<String> tokenize(String expression) {
    List<String> tokens = new ArrayList<>();
    int i = 0;
    while (i< expression.length()) {
      char c = expression.charAt(i);
      StringBuilder sb = new StringBuilder();

      if (Character.isDigit(c)|| c == '.'|| (c == '-' && i == 0)) {
        while (i < expression.length()
                && (Character.isDigit(expression.charAt(i))
                || expression.charAt(i) == '.'
                || (expression.charAt(i) == '-' && i == 0))) {
          if (expression.charAt(0) == '-' && i == 0) {
            sb.append('-');
            i++;
          }
          sb.append(expression.charAt(i));
          i++;
        }
        tokens.add(sb.toString());
      }else if (c == '+' || c == '-' || c == '*' || c == '/') {
        tokens.add(String.valueOf(c));
        i++;
      } else if (Character.isWhitespace(c)) {
        i++;
      } else {
        throw new IllegalArgumentException("Invalid character: " + c);
      }
    }
    return tokens;
  }


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
    } else if (operator.equals("*")) {
      return a * b;
    } else if (operator.equals("/")) {
      return a / b;
    } else {
      throw new IllegalArgumentException("Operator not supported");
    }
  }

}
