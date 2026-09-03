package com.syndica.backend.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.syndica.backend.domain.dto.DocumentDownload;
import com.syndica.backend.domain.models.Document;
import com.syndica.backend.domain.repositories.DocumentRepository;
import com.syndica.backend.execptions.NotFoundException;


@Service
public class DocumentService {
    private static final Map<String, String> ALLOWED_FILE_TYPES = Map.of(
        "application/pdf", ".pdf",
        "image/jpeg", ".jpg",
        "image/png", ".png",
        "image/gif", ".gif"
    );

    private final DocumentRepository documentRepository;
    private final DocumentLocalService documentLocalService;
    private final Tika tika;

    public DocumentService(
        DocumentRepository documentRepository,
        DocumentLocalService documentLocalService,
        Tika tika
    ){
        this.documentRepository = documentRepository;
        this.documentLocalService = documentLocalService;
        this.tika = tika;
    }

    @Transactional
    public Document uploadDocument(MultipartFile file) throws IOException {
        String contentType = tika.detect(file.getInputStream());
        String extension = ALLOWED_FILE_TYPES.get(contentType);

        UUID key = UUID.randomUUID();
        String filename = key + extension;

        this.documentLocalService.upload(filename, file);

        return documentRepository.save(
            Document.builder()
                .id(key)
                .originalFilename(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(contentType)
                .bucketKey(filename)
                .createdAt(Instant.now())
                .build()
        );
    }

    // @Transactional
    // public DocumentDownload getDocument(UUID uuid){
    //     Document document = documentRepository
    //         .findById(uuid)
    //         .orElseThrow(NotFoundException::new);

    //     ResponseInputStream<GetObjectResponse> content =
    //         documentStorageService.getObject(document.getBucketKey());

    //     return new DocumentDownload(
    //         content,
    //         document.getContentType(),
    //         document.getOriginalFilename(),
    //         content.response().contentLength()
    //     );
    // }

    
    public ResponseEntity<InputStreamResource> createDocumentResponse(
        DocumentDownload document,
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