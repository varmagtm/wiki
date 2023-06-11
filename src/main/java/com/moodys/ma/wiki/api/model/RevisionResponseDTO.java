package com.moodys.ma.wiki.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RevisionResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
}
