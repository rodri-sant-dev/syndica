package com.syndica.backend.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.syndica.backend.execptions.NotFoundException;


@Service
public class DocumentLocalService {
    @Value("${storage.my-local-storage}")
    private String uploadDir;

    public void upload(String filename, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file");

        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(targetDir);

        Path targetPath = targetDir.resolve(filename).normalize();

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource load(String filename) {
        try {
            Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = targetDir.resolve(filename).normalize();

            if (!filePath.startsWith(targetDir)) {
                throw new SecurityException("Caminho de arquivo inválido");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("File does not found: " + filename);
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error to load file", e);
        }
    }

    public void delete(String filename) throws IOException {
        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = targetDir.resolve(filename).normalize();

        if (!filePath.startsWith(targetDir)) {
            throw new SecurityException("path to remove file contain error");
        }

        Files.deleteIfExists(filePath);
    }

}