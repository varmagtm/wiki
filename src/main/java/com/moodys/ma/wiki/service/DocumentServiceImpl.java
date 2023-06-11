package com.moodys.ma.wiki.service;

import com.moodys.ma.wiki.api.model.DocumentDTO;
import com.moodys.ma.wiki.api.model.RevisionResponseDTO;
import com.moodys.ma.wiki.dao.entity.Document;
import com.moodys.ma.wiki.dao.entity.DocumentRevision;
import com.moodys.ma.wiki.dao.repo.DocumentRepository;
import com.moodys.ma.wiki.exception.DataAccessException;
import com.moodys.ma.wiki.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public List<String> getAllTitles() throws NoDataFoundException, DataAccessException {
        List<String> allTitles;
        try {
            log.debug("Getting all titles");
            allTitles = this.documentRepository.findAllTitles();
        } catch (Exception e) {
            log.error("Error in getting all titles");
            throw new DataAccessException("Error in getting all titles", e);
        }
        if (CollectionUtils.isEmpty(allTitles)) {
            log.warn("No titles found in DB");
            throw new NoDataFoundException("No titles found in DB");
        }
        return allTitles;
    }

    @Override
    public List<RevisionResponseDTO> getRevisions(String docTitle) throws NoDataFoundException {
        List<DocumentRevision> revisions;
        try {
            log.debug("Getting revisions for title [{}]", docTitle);
            revisions = this.documentRepository.findRevisionsByTitle(docTitle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (CollectionUtils.isEmpty(revisions)) {
            log.warn("No revisions found for title [{}]", docTitle);
            throw new NoDataFoundException("No titles found in DB");
        }
        // Transform and return the response
        return mapRevisionEntityToResponseDTO(revisions);
    }

    private List<RevisionResponseDTO> mapRevisionEntityToResponseDTO(
            List<DocumentRevision> revisions) {
        return revisions.stream()
                .map(revision -> RevisionResponseDTO.builder()
                        .id(revision.getId())
                        .createdAt(revision.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO findLatestDocumentAtTimestamp(String title, LocalDateTime timestamp)
            throws NoDataFoundException {
        log.debug("Finding latest document with title [{}] at timestamp [{}]",
                title, timestamp);
        List<DocumentRevision> revisions;
        try {
            revisions = this.documentRepository.findLatestDocumentAtTimestamp(title, timestamp);
        } catch (Exception e) {
            throw new DataAccessException("Error while finding latest document at given timestamp", e);
        }
        if (CollectionUtils.isEmpty(revisions)) {
            throw new NoDataFoundException("No revision found for the " +
                    "given document title and timestamp");
        }
        return mapDocumentRevisionToDTO(revisions.get(0));
    }

    private DocumentDTO mapDocumentRevisionToDTO(DocumentRevision revision) {
        Document document = revision.getDocument();
        return DocumentDTO.builder()
                .title(document.getTitle())
                .documentId(document.getId())
                .revisionId(revision.getId())
                .content(revision.getContent())
                .revisionCreatedAt(revision.getCreatedAt())
                .build();
    }

    @Override
    public DocumentDTO findLatestDocument(String title) throws NoDataFoundException {
        log.debug("Finding latest document with title [{}]", title);
        List<DocumentRevision> revisions;
        try {
            revisions = this.documentRepository.findLatestContentByTitle(title);
        } catch (Exception e) {
            throw new DataAccessException("Error while finding latest document at given timestamp", e);
        }
        if (CollectionUtils.isEmpty(revisions)) {
            throw new NoDataFoundException("No revisions found for the " +
                    "document with the given title");
        }
        return mapDocumentRevisionToDTO(revisions.get(0));
    }

    @Override
    public Long updateDocument(String docTitle, String content) throws NoDataFoundException, DataAccessException {
        log.debug("Updating document with title [{}]", docTitle);
        long revisionId;
        try {
            Document document = this.documentRepository.findDocumentByTitle(docTitle);
            if (document == null) {
                throw new NoDataFoundException("No document found with the given title");
            }
            document.setUpdatedAt(LocalDateTime.now());
            DocumentRevision newRevision = new DocumentRevision(content);
            newRevision.setDocument(document);
            List<DocumentRevision> revisions = document.getRevisions();
            revisions.add(newRevision);
            this.documentRepository.save(document);
            revisionId = revisions.get(revisions.size() - 1).getId();
        } catch (NoDataFoundException e) {
            log.error("No document found with the title - " + docTitle);
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error while finding latest document at given timestamp", e);
        }
        return revisionId;
    }
}
