package com.syndica.backend.service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.syndica.backend.domain.models.Document;
import com.syndica.backend.domain.models.UserOutbox;
import com.syndica.backend.domain.repositories.DocumentRepository;
import com.syndica.backend.domain.repositories.UserOutboxRepository;
import com.syndica.backend.execptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserOutboxService {
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final UserOutboxRepository userOutboxRepository;

    public UserOutboxService(
        DocumentService documentService,
        DocumentRepository documentRepository,
        UserOutboxRepository userOutboxRepository
    ){
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.userOutboxRepository = userOutboxRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void managerUserOutbox(){
        log.info("Manager app started");
        userOutboxRepository.findAllByStatus("PENDING")
        .forEach(this::updateUserOutbox);
    }

    @Transactional
    public void updateUserOutbox(UserOutbox userOutbox){
        log.info("Item started");

        Map<String, String> payloadMap = userOutbox.getPayload();
        UUID documentKey = UUID.fromString(payloadMap.get("id"));

        Document document = documentRepository
            .findById(documentKey)
            .orElseThrow(NotFoundException::new);
        
        try {
            documentService.uploadStorageDocumentAndDeleteLocal(document);
            userOutbox.setStatus("COMPLETED");
            log.info("Analysis of document key {} completed", document.getId());
            
        } catch (IOException e) {
            userOutbox.setStatus("FAILED");
            log.error("Analysis of document key {} failed", document.getId(), e);
        }

        userOutboxRepository.save(userOutbox);
    }
}
