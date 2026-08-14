package com.syndica.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.service.UserService;

@RestController
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.getUser(user.getId()));
    }
}
