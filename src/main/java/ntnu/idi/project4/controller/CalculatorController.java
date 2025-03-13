package ntnu.idi.project4.controller;

import ntnu.idi.project4.dto.CalculationRequest;
import ntnu.idi.project4.dto.CalculationResponse;
import ntnu.idi.project4.model.Calculation;
import ntnu.idi.project4.service.CalculatorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * REST controller for the calculator service.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class CalculatorController {

  @Autowired
  private final CalculatorService calculatorService;

  Logger logger = LoggerFactory.getLogger(CalculatorController.class);

  public CalculatorController (CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  /**
   * REST endpoint for calculating an expression.
   *
   * @param request the calculation request
   * @return the calculation response
   */
  @PostMapping("/calc")
  public ResponseEntity<CalculationResponse> calc(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: " + request.getExpression());
    CalculationResponse response = calculatorService.calculateAndSave(request);
    logger.info("Returning calculation response: " + response.getResult());
    return ResponseEntity.ok(response);
  }

  /**
   * REST endpoint for getting the most recent calculations.
   *
   * @param userId the user id
   * @return the most recent calculations
   */
  @GetMapping("/{userId}/recent")
  public ResponseEntity<List<Calculation>> getRecentCalculations(@PathVariable int userId) {
    logger.info("Received request for recent calculations for user: " + userId);
    List<Calculation> calculations = calculatorService.getRecentCalculations(userId);
    logger.info("Returning recent calculations: " + calculations);
    return ResponseEntity.ok(calculations);
  }
}
