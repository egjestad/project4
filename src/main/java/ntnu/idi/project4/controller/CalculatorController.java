package ntnu.idi.project4.controller;

import ntnu.idi.project4.dto.CalculationRequest;
import ntnu.idi.project4.dto.CalculationResponse;
import ntnu.idi.project4.model.Calculation;
import ntnu.idi.project4.security.TokenUtil;
import ntnu.idi.project4.service.CalculatorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * REST controller for the calculator service.
 */
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
public class CalculatorController {

  @Autowired
  private final CalculatorService calculatorService;
  private final TokenUtil tokenUtil;
  Logger logger = LoggerFactory.getLogger(CalculatorController.class);

  public CalculatorController (CalculatorService calculatorService, TokenUtil tokenUtil) {
    this.calculatorService = calculatorService;
    this.tokenUtil = tokenUtil;
  }

  /**
   * REST endpoint for calculating an expression.
   *
   * @param request the calculation request
   * @return the calculation response
   */
  @PostMapping("/calc")
  public ResponseEntity<CalculationResponse> calc(@RequestBody CalculationRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    logger.info("Received calculation request: " + request.getExpression());
    logger.info("Recieved token: " + token);

    int userId = tokenUtil.extractUserId(token);
    if (userId == -1) {
      logger.info("Unauthorized request - invalid token");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    CalculationResponse response = calculatorService.calculateAndSave(userId, request.getExpression());
    logger.info("Returning calculation response: " + response.getResult());

    return ResponseEntity.ok(response);
  }

  /**
   * REST endpoint for getting the most recent calculations.
   *
   * @return the most recent calculations
   */
  @GetMapping("/recent")
  public ResponseEntity<List<Calculation>> getRecentCalculations(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    logger.info("Received request for recent calculations");

    int userId = tokenUtil.extractUserId(token);
    if (userId == -1) {
      logger.info("Unauthorized request");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    logger.info("Received request for recent calculations for user: " + userId);
    List<Calculation> calculations = calculatorService.getRecentCalculations(userId);
    logger.info("Returning recent calculations: " + calculations);
    return ResponseEntity.ok(calculations);
  }
}
