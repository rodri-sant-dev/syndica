package com.syndica.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.domain.dto.RefreshTokenRequestDTO;
import com.syndica.backend.domain.dto.TokenPair;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.execptions.UserDoesNotExistExecption;
import com.syndica.backend.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;



@RestController
@RequestMapping("token/")
public class TokenController {
    
    private final AuthService authService;

    public TokenController(
        AuthService authService
    ){
        this.authService = authService;
    }

    @SecurityRequirements()
    @PostMapping("/login")
    public ResponseEntity<TokenPair> createToken(@Valid LoginDTO loginDTO){
        User user = authService.userIsValid(loginDTO).orElseThrow(UserDoesNotExistExecption::new);
        TokenPair tokenPair = authService.login(user);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(tokenPair);
    }

    @SecurityRequirements()
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refreshToken(@Valid RefreshTokenRequestDTO refreshTokenRequestDTO){
        
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(authService.refreshToken(refreshTokenRequestDTO));
    }
}
