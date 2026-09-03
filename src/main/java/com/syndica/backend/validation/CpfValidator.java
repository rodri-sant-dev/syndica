package com.syndica.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        if (!cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstCheckDigit = calculateCheckDigit(cpf, 9);
        int secondCheckDigit = calculateCheckDigit(cpf, 10);

        return firstCheckDigit == Character.digit(cpf.charAt(9), 10)
            && secondCheckDigit == Character.digit(cpf.charAt(10), 10);
    }

    private int calculateCheckDigit(String cpf, int length) {
        int weight = length + 1;
        int sum = 0;

        for (int index = 0; index < length; index++) {
            sum += Character.digit(cpf.charAt(index), 10) * weight--;
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
