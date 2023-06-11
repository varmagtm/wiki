package com.moodys.ma.wiki.service;

import com.moodys.ma.wiki.api.model.DocumentDTO;
import com.moodys.ma.wiki.api.model.RevisionResponseDTO;
import com.moodys.ma.wiki.exception.DataAccessException;
import com.moodys.ma.wiki.exception.NoDataFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentService {
    List<String> getAllTitles() throws NoDataFoundException, DataAccessException;

    List<RevisionResponseDTO> getRevisions(String docTitle)
            throws NoDataFoundException;

    DocumentDTO findLatestDocumentAtTimestamp(String title, LocalDateTime timestamp)
            throws NoDataFoundException;

    DocumentDTO findLatestDocument(String title) throws NoDataFoundException;

    Long updateDocument(String docTitle, String content)
            throws NoDataFoundException, DataAccessException;
}
