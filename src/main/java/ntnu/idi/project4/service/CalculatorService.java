package ntnu.idi.project4.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

  public double calculate(String expression) {
    List<String> tokens = tokenize(expression);
    double result = Double.parseDouble(tokens.getFirst());
    for (int i = 1; i < tokens.size(); i += 2) {
      String operator = tokens.get(i);
      double operand = Double.parseDouble(tokens.get(i + 1));
      result = calc(result, operand, operator);
    }
    return result;
  }

  private List<String> tokenize(String expression) {
    List<String> tokens = new ArrayList<>();
    int i = 0;
    while (i< expression.length()) {
      char c = expression.charAt(i);
      if (Character.isDigit(c)|| c == '.') {
        StringBuilder sb = new StringBuilder();
        while ((i < expression.length()) && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
          sb.append(expression.charAt(i));
          i++;
        }
        tokens.add(sb.toString());
      }else if (c == '+' || c == '-' || c == 'x' || c == '/') {
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
    } else if (operator.equals("x")) {
      return a * b;
    } else if (operator.equals("/")) {
      return a / b;
    } else {
      throw new IllegalArgumentException("Operator not supported");
    }
  }

}
