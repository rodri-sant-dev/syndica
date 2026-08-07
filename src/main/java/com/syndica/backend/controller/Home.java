package com.syndica.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class Home {
    @GetMapping
    public String home(){
        return "UsuarioLogado";
    }
}
