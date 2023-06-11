package com.moodys.ma.wiki.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RevisionDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
