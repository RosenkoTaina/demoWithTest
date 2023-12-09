package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmployeeServiceEM {

    Employee createWithJpa(Employee employee);

    void deleteByIdWithJpa(Integer id);

    Employee updateByIdWithJpa(Integer id, Employee employee);

    Set<String> findAllCountriesWithJpa();

    List<Employee> getAllEM();

    void saveDocument(LocalDateTime expireDate, String number, Boolean isHandled, String uuid, Integer employeeId);

    void handleDocument(Integer employeeId);
}
