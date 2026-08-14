package com.syndica.backend.infra.exceptions;

import java.time.Instant;
import java.util.Map;

public record ApiError(int status, String message, Instant timestamp, Map<String, String> details) {

    public ApiError(int status, String message, Instant timestamp) {
        this(status, message, timestamp, null);
    }
}