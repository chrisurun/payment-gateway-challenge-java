package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;
import com.checkout.payment.gateway.model.ErrorsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CommonExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  public static String PAYMENT_NOT_FOUND = "Payment not found";
  public static String INVALID_REQUEST = "Invalid request";

  @ExceptionHandler(EventProcessingException.class)
  public ResponseEntity<ErrorResponse> handleException(EventProcessingException ex) {
    LOG.error("An exception happened", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MissingEntityException.class)
  public ResponseEntity<ErrorResponse> handleException(MissingEntityException ex) {
    LOG.error("Entity not found", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), NOT_FOUND);
  }

  @ExceptionHandler(ClientException.class)
  public ResponseEntity<ErrorResponse> handleException(ClientException ex) {
    LOG.error("Upstream error while processing request", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), BAD_REQUEST);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorsResponse> handleException(ValidationException ex) {
    LOG.info("Invalid user request", ex);
    return new ResponseEntity<>(new ErrorsResponse(
        INVALID_REQUEST,
        ex.getFieldErrors()),
        BAD_REQUEST);
  }

}
