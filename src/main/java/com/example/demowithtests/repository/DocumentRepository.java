package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Transactional
    @Query(value = "INSERT INTO documents(expire_date, number, is_handled, uuid) VALUES (:expireDate, :number, :isHandled, :uuid) RETURNING id", nativeQuery = true)
    Integer saveDocument(LocalDateTime expireDate, String number, Boolean isHandled, String uuid);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE documents SET is_handled = TRUE WHERE id = :id", nativeQuery = true)
    Integer setDocumentIsHandled(Integer id);

}
