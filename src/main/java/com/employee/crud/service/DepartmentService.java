package com.employee.crud.service;

import com.employee.crud.dto.DepartmentDTO;
import com.employee.crud.entity.Department;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DepartmentService {

    public List<DepartmentDTO> getDepartments();

    DepartmentDTO getDepartmentsById(Integer id);

    void createDepartment(@Valid DepartmentDTO DepartmentDTO);
    DepartmentDTO updateDepartment(@Valid DepartmentDTO DepartmentDTO);

    void deleteDepartment(Long id);

    Page<Department> getDepartmentsPage(PageRequest of);
}
