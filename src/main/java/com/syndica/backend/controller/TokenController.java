package com.syndica.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.domain.dto.LoginResponseDTO;
import com.syndica.backend.domain.dto.RefreshTokenRequestDTO;
import com.syndica.backend.domain.dto.TokenPairDTO;
import com.syndica.backend.domain.mappers.UserMapper;
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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        User user = authService.userIsValid(loginDTO).orElseThrow(UserDoesNotExistExecption::new);

        TokenPairDTO tokenPair = authService.login(user, loginDTO.remember());
        
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
           UserMapper.toUserResponseDTO(user),
            tokenPair
        ); 

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(loginResponseDTO);
    }

    @SecurityRequirements()
    @PostMapping("/refresh")
    public ResponseEntity<TokenPairDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        TokenPairDTO tokenPair = authService.refreshToken(refreshTokenRequestDTO);
       
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tokenPair);
    }

    
    @SecurityRequirements()
    @PostMapping("/logout")
    public ResponseEntity<Void> blacklistToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        authService.blacklistToken(refreshTokenRequestDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
