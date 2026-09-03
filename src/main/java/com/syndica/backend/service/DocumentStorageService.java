package com.syndica.backend.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class DocumentStorageService {

    private final S3Client s3Client;

    @Value("${storage.bucket-name}")
    private String bucketName;

    public DocumentStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(String fileName, InputStream fileStream, long contentLength, String contentType) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build(),
            RequestBody.fromInputStream(fileStream, contentLength)
        );
    }

    public ResponseInputStream<GetObjectResponse> getObject(String fileName) {
        return s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()
        );
    }
}
