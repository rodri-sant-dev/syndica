package com.syndica.backend.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import com.syndica.backend.domain.models.Document;
import com.syndica.backend.domain.repositories.DocumentRepository;
import com.syndica.backend.execptions.NotFoundException;

import jakarta.transaction.Transactional;

@Service
public class DocumentService {
    private static final Map<String, String> ALLOWED_FILE_TYPES = Map.of(
        "application/pdf", ".pdf",
        "image/jpeg", ".jpg",
        "image/png", ".png",
        "image/gif", ".gif"
    );

    private final DocumentRepository documentRepository;
    private final DocumentStorageService documentStorageService;
    private final Tika tika;

    public DocumentService(
        DocumentRepository documentRepository,
        DocumentStorageService documentStorageService,
        Tika tika
    ){
        this.documentRepository = documentRepository;
        this.documentStorageService = documentStorageService;
        this.tika = tika;
    }

    @Transactional
    public Document uploadDocument(MultipartFile file) throws IOException {
        String contentType = tika.detect(file.getInputStream());
        String extension = ALLOWED_FILE_TYPES.get(contentType);

        UUID key = UUID.randomUUID();
        String bucketKey = key + extension;

        this.documentStorageService.upload(
            bucketKey,
            file.getInputStream(),
            file.getSize(),
            contentType
        );

        return documentRepository.save(
            Document.builder()
                .id(key)
                .originalFilename(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(contentType)
                .bucketKey(bucketKey)
                .createdAt(Instant.now())
                .build()
        );
    }

    @Transactional
    public DocumentDownload getDocument(UUID uuid){
        Document document = documentRepository
            .findById(uuid)
            .orElseThrow(NotFoundException::new);

        ResponseInputStream<GetObjectResponse> content =
            documentStorageService.getObject(document.getBucketKey());

        return new DocumentDownload(
            content,
            document.getContentType(),
            document.getOriginalFilename(),
            content.response().contentLength()
        );
    }

    public record DocumentDownload(
        ResponseInputStream<GetObjectResponse> content,
        String contentType,
        String originalFilename,
        Long contentLength
    ) {}
}