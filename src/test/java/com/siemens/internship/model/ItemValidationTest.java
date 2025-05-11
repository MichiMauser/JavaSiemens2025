package com.siemens.internship.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

//Comment: Added model verification for email validation
// For the valid mail assert should log True because there are no violations
// For the 2nd and 3rd functions I logged the violations
class ItemValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidEmailAndNotBlank() {
        Item validItem = new Item(null, "Test", "Desc", "New", "email@example.com");
        Set<ConstraintViolation<Item>> violations = validator.validate(validItem);

        assertEquals(0, violations.size());
    }

    @Test
    void testBlankEmail() {
        Item item = new Item(null, "Test", "Desc", "New", "");
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        violations.forEach(v -> System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage()));

    }

    @Test
    void testInvalidEmailFormat() {
        Item item = new Item(null, "Test", "Desc", "New", "not_an_email");
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        violations.forEach(v -> System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage()));

    }
}
