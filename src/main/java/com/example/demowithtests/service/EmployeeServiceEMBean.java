package com.example.demowithtests.service;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceEMBean implements EmployeeServiceEM {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param employee
     * @return
     */
    @Override
    @Transactional //jakarta
    public Employee createWithJpa(Employee employee) {
        return entityManager.merge(employee);
        /*entityManager.persist(employee);
        entityManager.flush();
        return entityManager.find(Employee.class, employee);*/
    }

    /**
     * @return
     */
    @Override
    @Transactional //jakarta
    public Set<String> findAllCountriesWithJpa() {
        return entityManager.createQuery("select distinct country from Employee", String.class).getResultStream().collect(Collectors.toSet());
    }

    /**
     * @param id
     * @param employee
     * @return
     */
    @Override
    @Transactional //jakarta
    public Employee updateByIdWithJpa(Integer id, Employee employee) {
        Employee refreshEmployee = Optional.ofNullable(entityManager.find(Employee.class, id))
                .orElseThrow(() -> new RuntimeException("id = " + employee.getId()));
        return entityManager.merge(refreshEmployee);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional //jakarta
    public void deleteByIdWithJpa(Integer id) {
        Optional<Employee> employee = Optional.ofNullable(entityManager.find(Employee.class, id));
        entityManager.remove(employee);
    }

    @Override
    @Transactional
    public List<Employee> getAllEM() {
        return entityManager.createNativeQuery("SELECT * FROM users WHERE is_deleted = false", Employee.class).getResultList();
    }

    @Override
    @Transactional
    public void saveDocument(LocalDateTime expireDate, String number, Boolean isHandled, String uuid, Integer employeeId) {
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

    }

    @Override
    @Transactional
    public void handleDocument(Integer employeeId) {
      Employee employee = Optional.ofNullable(entityManager.find(Employee.class, employeeId))
                .orElseThrow(() -> new RuntimeException("id = " + employeeId));
      Document document = Optional.ofNullable(employee.getDocument())
                .orElseThrow(() -> new RuntimeException("Document not found"));;
     document.setIsHandled(Boolean.TRUE);
     employee.setDocument(document);
     entityManager.persist(employee);

    }


}
