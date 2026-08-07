package com.syndica.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.service.AuthService;
import com.syndica.backend.service.JwtService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("token/")
public class TokenController {
    private final JwtService jwtService;
    private final AuthService authService;

    public TokenController(
        JwtService jwtService,
        AuthService authService
    ){
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public String gerarToken(@Valid LoginDTO loginDTO){
        return authService.userIsValid(loginDTO)
            .map(user -> "Usuário Válido")
            .orElse("Usuário não Válido");
    }
}
