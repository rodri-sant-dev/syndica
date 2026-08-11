package com.syndica.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.models.User;

@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String home(@AuthenticationPrincipal User user){
        return "UsuarioLogado";
    }
}
