package ntnu.idi.project4.service;


import ntnu.idi.project4.dto.CalculationRequest;
import ntnu.idi.project4.dto.CalculationResponse;
import ntnu.idi.project4.model.Calculation;
import ntnu.idi.project4.repo.CalculationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CalculatorService {

  Logger logger = LoggerFactory.getLogger(CalculatorService.class);

  private final CalculationRepository calculationRepository;

  public CalculatorService(CalculationRepository calculationRepository) {
    this.calculationRepository = calculationRepository;
  }
  public void saveCalculation(int userId, String expression, double result) {
    Calculation calculation = new Calculation();
    calculation.setUserId(userId);
    calculation.setExpression(expression);
    calculation.setResult(result);
    calculationRepository.save(calculation);
  }

  public List<Calculation> getRecentCalculations(int userId) {
    return calculationRepository.findTenMostRecentCalculations(userId);
  }

  public List<Calculation> getRecentCalculations(int userId, int limit) {
    return calculationRepository.findRecentCalculations(userId, limit);
  }

  public CalculationResponse calculateAndSave(CalculationRequest request) {
    double result = calculate(request.getExpression());
    saveCalculation(request.getUserId(), request.getExpression(), result);
    return new CalculationResponse(result);
  }

  public double calculate(String expression) {
    logger.info("Calculating expression: {} ", expression);
    List<String> tokens = tokenize(expression);
    logger.debug("Tokenized expression: {} ", tokens);

    while (tokens.size() >= 3){
      // Multiplication and division have higher precedence than addition and subtraction
      if ((tokens.size() > 3)
              && (tokens.get(1).equals("+") || tokens.get(1).equals("-"))
              && (tokens.get(3).equals("*") || tokens.get(3).equals("/"))) {
        double result = calc(Double.parseDouble(tokens.get(2)), Double.parseDouble(tokens.get(4)), tokens.get(3));
        logger.debug("Calculated expression with higher precedence: {} {} {} = {}",
                tokens.get(2), tokens.get(3), tokens.get(4), result);
        tokens.remove(2);
        tokens.remove(2);
        tokens.remove(2);
        tokens.add(2, String.valueOf(result));

      } else {
        double result = calc(Double.parseDouble(tokens.get(0)), Double.parseDouble(tokens.get(2)), tokens.get(1));
        logger.debug("Calculated expression: {} {} {} = {}",
                tokens.get(0), tokens.get(1), tokens.get(2), result);
        tokens.remove(0);
        tokens.remove(0);
        tokens.remove(0);
        tokens.add(0,String.valueOf(result));
      }
    }
    double finalResult = Double.parseDouble(tokens.get(0));
    logger.info("Final result: {} ", finalResult);
    return finalResult;
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
        logger.error("Invalid character: {}", c);
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
      logger.error("Operator cannot be null");
      throw new IllegalArgumentException("Operator cannot be null");
    } else if (operator.equals("+")) {
      return a + b;
    } else if (operator.equals("-")) {
      return a - b;
    } else if (operator.equals("*")) {
      return a * b;
    } else if (operator.equals("/")) {
      if (b == 0) {
        logger.error("Division by zero");
      }
      return a / b;
    } else {
      logger.error("Operator not supported: {}", operator);
      throw new IllegalArgumentException("Operator not supported");
    }
  }

}
