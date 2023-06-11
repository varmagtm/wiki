package com.moodys.ma.wiki.api.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ErrorResponseDTO {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final String api;
}
