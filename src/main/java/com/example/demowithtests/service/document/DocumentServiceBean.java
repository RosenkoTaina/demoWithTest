package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Action;
import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.DocumentDto;
import com.example.demowithtests.repository.DocumentRepository;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.EmployeeServiceEMBean;
import com.example.demowithtests.util.exception.ResourceWasDeletedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceBean implements DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final DocumentServiceEM documentServiceEM;
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * @param document
     * @return
     */
    @Override
    public Document create(Document document) {
        document.setExpireDate(LocalDateTime.now().plusYears(5));
        return documentRepository.save(document);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document getById(Integer id) {
        return documentRepository.findById(id).orElseThrow();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document handlePassport(Integer id) {
        Document document = getById(id);
        if (document.getIsHandled()) {
            throw new RuntimeException();
        } else document.setIsHandled(Boolean.TRUE);
        return documentRepository.save(document);
    }

    /**
     * @param passportId
     * @param imageId
     * @return
     */
    @Override
    public Document addImage(Integer passportId, Integer imageId) {
        return null;
    }

    @Override
    public void attachDocument(DocumentDto documentDto) {
        Optional<Employee> employee = employeeRepository.findById(documentDto.employeeId);
        if (employee.isPresent()) {
            if (employee.get().getDocument()!=null){
                throw new IllegalArgumentException("Document already exist");
            }
            else {
                Integer documentId = documentServiceEM.saveDocument(documentDto.expireDate,documentDto.number, false, UUID.randomUUID().toString(), documentDto.employeeId);
                documentServiceEM.setLogForDocument(documentDto.employeeId, documentId, Action.CREATE);

            }
        }
    }

}
