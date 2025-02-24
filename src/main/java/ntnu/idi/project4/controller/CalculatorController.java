package ntnu.idi.project4.controller;

import ntnu.idi.project4.dto.CalculationRequest;
import ntnu.idi.project4.dto.CalculationResponse;
import ntnu.idi.project4.service.CalculatorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

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

  @PostMapping("/calc")
  public ResponseEntity<CalculationResponse> calc(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: " + request.getExpression());
    double result = calculatorService.calculate(request.getExpression());
    CalculationResponse response = new CalculationResponse(result);
    return ResponseEntity.ok(response);
  }

}
