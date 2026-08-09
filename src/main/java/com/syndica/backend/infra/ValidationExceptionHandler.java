package com.syndica.backend.infra;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.syndica.backend.execptions.UserDoesNotExistExecption;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(UserDoesNotExistExecption.class)
    public ResponseEntity<Map<String, String>> handleCredenciaisInvalidas(UserDoesNotExistExecption ex) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }
}
