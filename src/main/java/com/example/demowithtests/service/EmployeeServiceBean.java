package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.emailSevice.EmailSenderService;
import com.example.demowithtests.util.annotations.entity.ActivateCustomAnnotations;
import com.example.demowithtests.util.annotations.entity.Name;
import com.example.demowithtests.util.annotations.entity.ToLowerCase;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import com.example.demowithtests.util.exception.ResourceWasDeletedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class EmployeeServiceBean implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeServiceEMBean employeeServiceEMBean;
    private final EmailSenderService emailSenderService;

    @Override
    @ActivateCustomAnnotations({Name.class, ToLowerCase.class})
    // @Transactional(propagation = Propagation.MANDATORY)
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
        //return employeeRepository.saveAndFlush(employee);
    }

    /**
     * @param employee
     * @return
     */
    @Override
    public void createAndSave(Employee employee) {
        employeeRepository.saveEmployee(employee.getName(), employee.getEmail(), employee.getCountry(), String.valueOf(employee.getGender()));
    }

    @Override
    public List<Employee> getAll() {
        return employeeServiceEMBean.getAllEM();
    }

    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        log.debug("getAllWithPagination() - start: pageable = {}", pageable);
        Page<Employee> list = employeeRepository.findAll(pageable);
        log.debug("getAllWithPagination() - end: list = {}", list);
        return list;
    }

    @Override
    public Employee getById(Integer id) {
        var employee = employeeRepository.findById(id)
                // .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
                .orElseThrow(ResourceNotFoundException::new);
        /* if (employee.getIsDeleted()) {
            throw new EntityNotFoundException("Employee was deleted with id = " + id);
        }*/
        return employee;
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {
        return employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
    }

    @Override
    public void removeById(Integer id) {
        //repository.deleteById(id);
        var employee = employeeRepository.findById(id)
                // .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
                .orElseThrow(ResourceWasDeletedException::new);
        employee.set_deleted(true);
        // employeeRepository.delete(employee);
        employeeRepository.save(employee);
    }


   /* @Override
    public void removeById(Integer id) {
        employeeRepository.findById(id)
                .filter(this::IsEmployeePresent)
                .map(employee -> {
                    employee.setIsDeleted(Boolean.TRUE);
                    return employeeRepository.save(employee);
                })
                .orElseThrow(ResourceWasDeletedException::new);
    }*/

    @Override
    public void removeAll() {
        employeeRepository.deleteAll();
    }

    /*private boolean IsEmployeePresent(Employee employee) {
        Boolean isDeleted = employee.getIsDeleted();
        if (isDeleted != null && isDeleted.equals(Boolean.FALSE)) return true;
        else return false;
    }

    @Override
    public void removeAll() {
        List<Employee> list =
                employeeRepository.findAll().stream()
                        .filter(this::IsEmployeePresent)
                        .peek(emp -> emp.setIsDeleted(Boolean.TRUE))
                        .toList();

        employeeRepository.saveAll(list);
    }*/

    /*@Override
    public Page<Employee> findByCountryContaining(String country, Pageable pageable) {
        return employeeRepository.findByCountryContaining(country, pageable);
    }*/

    @Override
    public Page<Employee> findByCountryContaining(String country, int page, int size, List<String> sortList, String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        return employeeRepository.findByCountryContaining(country, pageable);
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }

    @Override
    public List<String> getAllEmployeeCountry() {
        log.info("getAllEmployeeCountry() - start:");
        List<Employee> employeeList = employeeRepository.findAll();
        List<String> countries = employeeList.stream()
                .map(country -> country.getCountry())
                .collect(Collectors.toList());
        /*List<String> countries = employeeList.stream()
                .map(Employee::getCountry)
                //.sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());*/

        log.info("getAllEmployeeCountry() - end: countries = {}", countries);
        return countries;
    }

    @Override
    public List<String> getSortCountry() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream()
                .map(Employee::getCountry)
                .filter(c -> c.startsWith("U"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> findEmails() {
        var employeeList = employeeRepository.findAll();

        var emails = employeeList.stream()
                .map(Employee::getEmail)
                .collect(Collectors.toList());

        var opt = emails.stream()
                .filter(s -> s.endsWith(".com"))
                .findFirst()
                .orElse("error?");
        return Optional.ofNullable(opt);
    }

    @Override
    public List<Employee> filterByCountry(String country) {
        return employeeRepository.findEmployeesByCountry(country);
    }

    @Override
    public Set<String> sendEmailsAllUkrainian() {
        var ukrainians = employeeRepository.findAllUkrainian()
                .orElseThrow(() -> new EntityNotFoundException("Employees from Ukraine not found!"));
        var emails = new HashSet<String>();
        ukrainians.forEach(employee -> {
            emailSenderService.sendEmail(
                    /*employee.getEmail(),*/
                    "kaluzny.oleg@gmail.com", //для тесту
                    "Need to update your information",
                    String.format(
                            "Dear " + employee.getName() + "!\n" +
                                    "\n" +
                                    "The expiration date of your information is coming up soon. \n" +
                                    "Please. Don't delay in updating it. \n" +
                                    "\n" +
                                    "Best regards,\n" +
                                    "Ukrainian Info Service.")
            );
            emails.add(employee.getEmail());
        });

        return emails;
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<Employee> findByNameContaining(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    /**
     * @param name
     * @param id
     * @return
     */
    @Override
    public void updateEmployeeByName(String name, Integer id) {
        /*var employee = employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
        return employee;*/

        employeeRepository.updateEmployeeByName(name, id);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Page<Employee> checkDuplicateEmails(String email, Pageable pageable) {
        return employeeRepository.findEmployeesByEmail(email, pageable);
    }
}
