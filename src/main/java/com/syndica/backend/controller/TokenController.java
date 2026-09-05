package com.syndica.backend.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.domain.dto.LoginResponseDTO;
import com.syndica.backend.domain.dto.RefreshTokenRequestDTO;
import com.syndica.backend.domain.dto.TokenPairDTO;
import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.mappers.UserMapper;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.execptions.UserDoesNotExistExecption;
import com.syndica.backend.service.AuthService;
import com.syndica.backend.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;


@RestController
@RequestMapping("token/")
public class TokenController {
    private final AuthService authService;
    private final UserService userService;

    public TokenController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    @SecurityRequirements()
    @PostMapping(value="/create-user", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(
        @RequestPart("userForCreateDTO") @Valid UserForCreateDTO userForCreateDTO,
        @RequestPart(value="perfilPhoto", required=false) MultipartFile perfilPhoto
    ) throws IOException {
        if (perfilPhoto == null || perfilPhoto.isEmpty()) {
            userService.saveUser(userForCreateDTO);
        } else {
            userService.saveUser(perfilPhoto, userForCreateDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @SecurityRequirements()
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        User user = authService.userIsValid(loginDTO).orElseThrow(UserDoesNotExistExecption::new);
        TokenPairDTO tokenPair = authService.login(user, loginDTO.remember());
        
        URI imageURI = null;

        if (user.getAvatarImage() != null) {
            imageURI = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/avatar-image/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        }

        LoginResponseDTO loginResponseDTO = LoginResponseDTO
        .builder()
        .user(UserMapper.toUserResponseDTO(user))
        .tokens(tokenPair)
        .imageURI(imageURI)
        .build(); 

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
