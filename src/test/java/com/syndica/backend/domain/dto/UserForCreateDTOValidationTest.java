package com.syndica.backend.domain.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserForCreateDTOValidationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void acceptsValidUserWithDefaultTheme() {
        UserForCreateDTO dto = UserForCreateDTO.builder()
            .fullname("Maria da Silva")
            .email("maria@example.com")
            .password("Valid@123")
            .cpf("52998224725")
            .build();

        assertEquals("SYSTEM", dto.theme());
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void acceptsValidUser() {
        UserForCreateDTO dto = validUser().build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void rejectsInvalidCpf() {
        UserForCreateDTO dto = validUser().cpf("52998224724").build();

        assertEquals(Set.of("cpf"), fieldsWithViolations(dto));
    }

    @Test
    void rejectsFormattedCpf() {
        UserForCreateDTO dto = validUser().cpf("529.982.247-25").build();

        assertEquals(Set.of("cpf"), fieldsWithViolations(dto));
    }

    @Test
    void rejectsWeakPassword() {
        UserForCreateDTO dto = validUser().password("password").build();

        assertEquals(Set.of("password"), fieldsWithViolations(dto));
    }

    @Test
    void rejectsUnsupportedTheme() {
        UserForCreateDTO dto = validUser().theme("BLUE").build();

        assertEquals(Set.of("theme"), fieldsWithViolations(dto));
    }

    @Test
    void rejectsMissingRequiredFields() {
        UserForCreateDTO dto = UserForCreateDTO.builder().build();

        assertEquals(
            Set.of("fullname", "email", "password", "cpf"),
            fieldsWithViolations(dto)
        );
    }

    private static UserForCreateDTO.UserForCreateDTOBuilder validUser() {
        return UserForCreateDTO.builder()
            .fullname("Maria da Silva")
            .email("maria@example.com")
            .password("Valid@123")
            .cpf("52998224725")
            .theme("SYSTEM");
    }

    private static Set<String> fieldsWithViolations(UserForCreateDTO dto) {
        return validator.validate(dto)
            .stream()
            .map(violation -> violation.getPropertyPath().toString())
            .collect(Collectors.toSet());
    }
}
