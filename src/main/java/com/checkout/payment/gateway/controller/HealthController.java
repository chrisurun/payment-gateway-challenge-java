package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.service.HealthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {

  private final HealthService healthService;

  public HealthController(HealthService healthService) {
    this.healthService = healthService;
  }

  @GetMapping("")
  public ResponseEntity<HealthResponse> getHealth() {
    return new ResponseEntity<>(healthService.getHealth(),  HttpStatus.OK);
  }
}
