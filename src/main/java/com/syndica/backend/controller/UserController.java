package com.syndica.backend.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndica.backend.domain.dto.DocumentDownload;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.UserRepository;
import com.syndica.backend.execptions.NotFoundException;
import com.syndica.backend.execptions.UserDoesNotExistExecption;
import com.syndica.backend.service.DocumentService;

@RestController
@RequestMapping("user/")
public class UserController {
    private final DocumentService documentService;
    private final UserRepository userRepository;

    public UserController(
        DocumentService documentService,
        UserRepository userRepository
    ){
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping(value="/avatar-image/{id}")
    public  ResponseEntity<InputStreamResource> getAvatarImage(
        @PathVariable UUID id
    ){
        User user = userRepository.findById(id)
        .orElseThrow(UserDoesNotExistExecption::new);

        if( user.getAvatarImage() == null){
            throw new NotFoundException("User not have a avatar image");
        }

        DocumentDownload document = documentService.getDocument(user.getAvatarImage());

        return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.parseMediaType(document.contentType()))
        .contentLength(document.contentLength())
        .header(
            "Content-Disposition",
            "inline; filename=\"" + document.originalFilename() + "\""
        )
        .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
        .body(new InputStreamResource(document.content()));
    }
    
}
