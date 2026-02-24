package com.employee.crud.repository;

import com.employee.crud.entity.Department;
import com.employee.crud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartment(Department department);
    List<Employee> findByDepartmentDid(Integer departmentId);
    Optional<Employee> findByEmail(String email);
}
