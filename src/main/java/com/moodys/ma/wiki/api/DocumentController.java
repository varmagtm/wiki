package com.moodys.ma.wiki.api;

import com.moodys.ma.wiki.api.model.DocumentDTO;
import com.moodys.ma.wiki.api.model.RevisionResponseDTO;
import com.moodys.ma.wiki.service.DocumentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public List<String> getDocumentTitles() {
        return this.documentService.getAllTitles();
    }

    @GetMapping("/{title}")
    public List<RevisionResponseDTO> getDocumentRevisions(@PathVariable String title) {
        return this.documentService.getRevisions(title);
    }

    @GetMapping("/{title}/{timestamp}")
    public DocumentDTO getDocumentAtTimestamp(
            @PathVariable String title,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp) {
        return this.documentService.findLatestDocumentAtTimestamp(title, timestamp);
    }

    @GetMapping("/{title}/latest")
    public DocumentDTO getLatestDocumentRevision(@PathVariable String title) {
        return this.documentService.findLatestDocument(title);
    }

    @PutMapping("/{title}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createDocumentRevision(
            @PathVariable String title,
            @RequestBody @NotBlank String newContent) {
        return this.documentService.updateDocument(title, newContent);
    }
}
