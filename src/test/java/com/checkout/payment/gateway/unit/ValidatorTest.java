package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.BaseTest;
import com.checkout.payment.gateway.util.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static com.checkout.payment.gateway.util.Validator.INVALID_CARD_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ValidatorTest extends BaseTest {

  @ParameterizedTest
  @ValueSource(strings = {"1234", "1234567890123456", "12345678901234567890"})
  void whenCardNumberIsValidatedInvalidCardNumberIsFlagged(String cardNumber)  {
    var validator = new Validator();
    validator.validateCardNumber(cardNumber);
    assertFalse(validator.isValid());
    var errors = validator.getErrors();
    assertEquals(1, errors.size());
    assertEquals("cardNumber", errors.get(0).getField());
    assertEquals(INVALID_CARD_NUMBER, errors.get(0).getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"4000000000000002", "4242424242424242", "2222405343248877"})
  void whenCardNumberIsValidatedValidCardNumberIsAccepted(String cardNumber)  {
    var validator = new Validator();
    validator.validateCardNumber(cardNumber);
    assertTrue(validator.isValid());
    var errors = validator.getErrors();
    assertEquals(0, errors.size());
  }
}
