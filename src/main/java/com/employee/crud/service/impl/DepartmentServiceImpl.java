package com.employee.crud.service.impl;

import com.employee.crud.dto.DepartmentDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.exception.DuplicateDepartmentException;
import com.employee.crud.exception.DepartmentNotFoundException;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.mapper.UserMapper;
import com.employee.crud.repository.DepartmentRepository;
import com.employee.crud.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {


    private DepartmentRepository departmentRepository;
   

    private UserMapper userMapper;

    @Override
    public List<DepartmentDTO> getDepartments() {
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty())
        {
            throw new UserNotFoundException("No departments found.");
        }
        return departments.stream().map(userMapper::mapDepartmentEntityToDepartmentDTO).toList();
    }

    @Override
    public DepartmentDTO getDepartmentsById(Integer id) {

        Optional<Department> existingDepartment = departmentRepository.findById(Long.valueOf(id));
        if(existingDepartment.isEmpty())
        {
            throw new DepartmentNotFoundException("Department not found.");
        }
        return existingDepartment.stream().map(userMapper::mapDepartmentEntityToDepartmentDTO).findFirst().get();
    }

    public void createDepartment(DepartmentDTO departmentDTO)
    {
        if(departmentRepository.existsByDname(departmentDTO.getDname()))
        {
            throw new DuplicateDepartmentException("Department name already exist.");
        }
        departmentRepository.save(userMapper.mapDepartmentDTOToDepartmentEntity(departmentDTO));
    }

    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO)
    {
        Department existingDepartment = departmentRepository.findById(Long.valueOf(departmentDTO.getDid()))
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        userMapper.updateEntityFromDto(departmentDTO, existingDepartment);

        departmentRepository.save(existingDepartment);
            return departmentDTO;
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department not found");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public Page<Department> getDepartmentsPage(PageRequest pageRequest) {
        return departmentRepository.findAll(pageRequest);
    }

}
