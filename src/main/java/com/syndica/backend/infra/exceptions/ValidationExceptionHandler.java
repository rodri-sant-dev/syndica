package com.syndica.backend.infra.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.syndica.backend.execptions.BusinessRuleException;
import com.syndica.backend.execptions.InvalidTokenException;
import com.syndica.backend.execptions.NotFoundException;
import com.syndica.backend.execptions.UserDoesNotExistExecption;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(UserDoesNotExistExecption.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(UserDoesNotExistExecption ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Violação de integridade de dados";
        String rootMessage = ex.getMostSpecificCause().getMessage();

        if (rootMessage != null) {
            if (rootMessage.contains("CPF")) {
                message = "There is already a registered user with this cpf ";
            } else if (rootMessage.contains("USERNAME")) {
                message = "There is already a registered user with this username";
            }
        }

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiError(HttpStatus.CONFLICT.value(), message, Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(HttpStatus.BAD_REQUEST.value(), "Erro de validação", Instant.now(), fieldErrors));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now()));
    }


    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRuleException(BusinessRuleException ex){
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now()));
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor", Instant.now()));
    }

}