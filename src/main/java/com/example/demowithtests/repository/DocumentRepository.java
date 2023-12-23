package com.example.demowithtests.repository;

import com.example.demowithtests.domain.Action;
import com.example.demowithtests.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO documenthistory(employee_id, document_id, action_id, date) VALUES (:employeeId, :documentId, :actionId, :date)", nativeQuery = true)
    void addDocumentHistory(Integer employeeId, Integer documentId, Integer actionId, LocalDateTime date);
}

