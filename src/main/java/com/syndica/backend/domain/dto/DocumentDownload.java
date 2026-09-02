package com.syndica.backend.domain.dto;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public record DocumentDownload(
    ResponseInputStream<GetObjectResponse> content,
    String contentType,
    String originalFilename,
    Long contentLength
) {}