package com.example.demowithtests.service.fillDataBase;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class LoaderServiceBean implements LoaderService {

    private final EmployeeRepository employeeRepository;


    @Override
    public void generateData() {
        log.info("Розпочинається генерація даних...");
        List<Employee> employees = createListEmployees();
        employeeRepository.saveAll(employees);
        log.info("Генерація даних завершена.");
    }

    @Override
    public long count() {
        log.info("Підрахунок кількості працівників...");
        long count = employeeRepository.count();
        log.info("Загальна кількість працівників: {}", count);
        return count;
    }

    public List<Employee> createListEmployees() {
        List<Employee> employees = new ArrayList<>();
        long seed = System.currentTimeMillis();
        Faker faker = new Faker(new Locale("uk"), new Random(seed));

        for (int i = 0; i < 2_000; i++) {
            String name = faker.name().fullName();
            String country = faker.address().country();
            String email = faker.internet().emailAddress();

            Set<Address> addresses = Set.copyOf(Arrays.asList(new Address(), new Address()));

            Employee employee = Employee.builder()
                    .name(name)
                    .country(country)
                    .email(email.toLowerCase().replaceAll(" ", "") + "@mail.com")
                    .addresses(addresses)
                    .build();

            employees.add(employee);
        }

        return employees;
    }
}
