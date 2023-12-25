package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Action;
import com.example.demowithtests.domain.Employee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DocumentServiceEM {

    Integer saveDocument(LocalDateTime expireDate, String number, Boolean isHandled, String uuid, Integer employeeId);
    void handleDocument(Integer employeeId);
    void setLogForDocument(Integer employeeId, Integer documentId, Action actionId);
    void removeById(Integer id);

}
