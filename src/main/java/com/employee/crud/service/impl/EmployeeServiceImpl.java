package com.employee.crud.service.impl;

import com.employee.crud.dto.EmployeeDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.entity.Employee;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.mapper.UserMapper;
import com.employee.crud.repository.EmployeeRepository;
import com.employee.crud.service.EmployeeService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;

    private final UserMapper userMapper;

    @Override
    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty())
        {
            throw new UserNotFoundException("No Employees found.");
        }
        return employees.stream().map(userMapper::mapEmployeeEntityToEmployeeDTO).toList();
    }

    @Override
    public EmployeeDTO getEmployeesById(Integer id) {

        Employee existingEmployee = employeeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UserNotFoundException("Employee not found."));

        return userMapper.mapEmployeeEntityToEmployeeDTO(existingEmployee);
    }

    public void createEmployee(EmployeeDTO employeeDTO)
    {
        employeeRepository.save(userMapper.mapEmployeeDTOToEmployeeEntity(employeeDTO));
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO)
    {
        Employee existingEmployee = employeeRepository.findById(Long.valueOf(employeeDTO.getEid()))
                .orElseThrow(() -> new UserNotFoundException("Employee not found"));
        userMapper.updateEntityFromDto(employeeDTO, existingEmployee);

        Department department = new Department();
        department.setDid(Long.valueOf(employeeDTO.getDid()));
        existingEmployee.setDepartment(department);

        employeeRepository.save(existingEmployee);
            return employeeDTO;
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new UserNotFoundException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> getEmployeesPage(PageRequest pageRequest) {
        return employeeRepository.findAll(pageRequest);
    }

    @Override
    public List<EmployeeDTO> getDepartmentWiseEmployee(Integer did)
    {
        List<Employee> employees= employeeRepository.findByDepartmentDid(did);
        if(employees.isEmpty())
        {
            throw new UserNotFoundException("No Employee Found Under this department");
        }

        return employees.stream().map(userMapper::mapEmployeeEntityToEmployeeDTO).toList();
    }

    @Override
    public EmployeeDTO getEmployeeByEmail(String email) {

        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Employee not found with email: " + email));

        return userMapper.mapEmployeeEntityToEmployeeDTO(employee);
    }


}
