package com.employee.crud.repository;

import com.employee.crud.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDname(String dname);
}
