package com.moodys.ma.wiki.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentDTO {
    private Long documentId;
    private String title;
    private Long revisionId;
    private String content;
    private LocalDateTime revisionCreatedAt;
    private LocalDateTime docCreatedAt;
}
