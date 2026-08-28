package com.syndica.backend.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.syndica.backend.service.DocumentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("user/")
public class UserController {
    private final DocumentService documentService;

    public UserController(
        DocumentService documentService
    ){
        this.documentService = documentService;
    }
    
    @SecurityRequirements()
    @PostMapping(value="/avatar-image", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDocument(@RequestPart("file") MultipartFile file) throws IOException{ 
        documentService.uploadDocument(file);

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
    }

    @SecurityRequirements()
    @GetMapping(value="/avatar-image")
    public ResponseEntity<InputStreamResource> getAvatarImage(@RequestParam UUID uuid){
        DocumentService.DocumentDownload document = documentService.getDocument(uuid);

        return documentResponse(document, "inline");
    }

    @SecurityRequirements()
    @GetMapping(value="/avatar-image/download")
    public ResponseEntity<InputStreamResource> downloadAvatarImage(@RequestParam UUID uuid){
        DocumentService.DocumentDownload document = documentService.getDocument(uuid);

        return documentResponse(document, "attachment");
    }

    private ResponseEntity<InputStreamResource> documentResponse(
        DocumentService.DocumentDownload document,
        String disposition
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(document.contentType()))
            .contentLength(document.contentLength())
            .header(
                "Content-Disposition",
                disposition + "; filename=\"" + document.originalFilename() + "\""
            )
            .body(new InputStreamResource(document.content()));
    }
    
}
