package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Action;
import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.DocumentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class DocumentServiceEMBean implements DocumentServiceEM {

    @PersistenceContext
    private EntityManager entityManager;

    private final DocumentRepository documentRepository;


    @Override
    @Transactional
    public Integer saveDocument(LocalDateTime expireDate, String number, Boolean isHandled, String uuid, Integer employeeId) {
        Document document = new Document();
        document.setExpireDate(expireDate);
        document.setNumber(number);
        document.setIsHandled(isHandled);
        document.setUuid(uuid);
        entityManager.persist(document);

        Employee employee = Optional.ofNullable(entityManager.find(Employee.class, employeeId))
                .orElseThrow(() -> new RuntimeException("id = " + employeeId));
        employee.setDocument(document);
        entityManager.persist(employee);
        return document.getId();
    }

    @Override
    @Transactional
    public void handleDocument(Integer employeeId) {
        Employee employee = Optional.ofNullable(entityManager.find(Employee.class, employeeId))
                .orElseThrow(() -> new RuntimeException("id = " + employeeId));
        Document document = Optional.ofNullable(employee.getDocument())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setIsHandled(Boolean.TRUE);
        employee.setDocument(document);
        entityManager.persist(employee);
        setLogForDocument(employeeId, document.getId(), Action.UPDATE);

    }


    public void setLogForDocument(Integer employeeId, Integer documentId, Action actionId) {
        documentRepository.addDocumentHistory(employeeId, documentId, actionId.getValue(), LocalDateTime.now());
    }


    @Override
    @Transactional
    public void removeById(Integer id) {
        Employee employee = Optional.ofNullable(entityManager.find(Employee.class, id))
               .orElseThrow(() -> new RuntimeException("id = " + id));
        Document document = Optional.ofNullable(employee.getDocument())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.set_deleted(true);
        employee.setDocument(null);

        entityManager.persist(employee);
        entityManager.persist(document);

        Integer documentId =  document.getId();
        setLogForDocument(employee.getId(), documentId, Action.DELETE);

    }
}


