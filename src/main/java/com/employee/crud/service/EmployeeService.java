package com.employee.crud.service;

import com.employee.crud.dto.EmployeeDTO;
import com.employee.crud.entity.Employee;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface EmployeeService {

    public List<EmployeeDTO> getEmployees();

    EmployeeDTO getEmployeesById(Integer id);

    void createEmployee(@Valid EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(@Valid EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    Page<Employee> getEmployeesPage(PageRequest of);

    List<EmployeeDTO> getDepartmentWiseEmployee(Integer did);

    EmployeeDTO getEmployeeByEmail(String email);
}
