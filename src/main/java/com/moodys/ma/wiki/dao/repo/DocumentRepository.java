package com.moodys.ma.wiki.dao.repo;

import com.moodys.ma.wiki.dao.entity.Document;
import com.moodys.ma.wiki.dao.entity.DocumentRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    @Query("SELECT r FROM DocumentRevision r WHERE r.document.title = :title AND " +
            "r.createdAt <= :timestamp ORDER BY r.createdAt DESC")
    List<DocumentRevision> findLatestDocumentAtTimestamp(
            @Param("title") String title,
            @Param("timestamp") LocalDateTime timestamp
    );

    @Query("SELECT new DocumentRevision(r.id, r.createdAt) FROM Document d JOIN d.revisions r WHERE d.title = :title")
    List<DocumentRevision> findRevisionsByTitle(String title);

    @Query("SELECT new DocumentRevision(r.id, r.createdAt, r.content, d) FROM DocumentRevision r, Document d " +
            "WHERE r.document.title = :title AND d.id = r.document.id ORDER BY r.createdAt DESC")
    List<DocumentRevision> findLatestContentByTitle(String title);

    @Query("SELECT d.title FROM Document d")
    List<String> findAllTitles();

    Document findDocumentByTitle(String title);
}
