package ntnu.idi.project4.controller;

import ntnu.idi.project4.service.CalculatorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

/**
 * REST controller for the calculator service.
 */
@RestController
public class CalculatorController {

  @Autowired
  private final CalculatorService calculatorService;

  Logger logger = LoggerFactory.getLogger(CalculatorController.class);

  public CalculatorController (CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @RequestMapping("/")
  public String index() {
    logger.info("Request to /");
    return "Hello World!";
  }

}
