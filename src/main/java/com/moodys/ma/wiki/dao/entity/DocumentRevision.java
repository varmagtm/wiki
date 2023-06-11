package com.moodys.ma.wiki.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DOCUMENT_REVISION")
@NoArgsConstructor
@Data
public class DocumentRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Column(nullable = false)
    private String content;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    public DocumentRevision(String content) {
        this.content = content;
    }

    public DocumentRevision(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public DocumentRevision(Long id, LocalDateTime createdAt, String content, Document document) {
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.document = document;
    }

}
