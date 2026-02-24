package com.employee.crud.mapper;

import com.employee.crud.dto.DepartmentDTO;
import com.employee.crud.dto.EmployeeDTO;
import com.employee.crud.dto.UserDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.entity.Employee;
import com.employee.crud.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "department.did", target = "did")
   EmployeeDTO mapEmployeeEntityToEmployeeDTO(Employee employee);

    @Mapping(source = "did", target = "department.did")
    Employee mapEmployeeDTOToEmployeeEntity(EmployeeDTO employeeDTO);


    @Mapping(source = "did", target = "department.did")
    void updateEntityFromDto(EmployeeDTO dto, @MappingTarget Employee entity);


   UserDTO mapUserEntityToUserDTO(User user);
    @Mapping(target = "pwd", expression = "java(passwordEncoder.encode(userDTO.getPwd()))")
    User mapUserDTOToUserEntity(UserDTO userDTO, @Context PasswordEncoder passwordEncoder);

    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);

    DepartmentDTO mapDepartmentEntityToDepartmentDTO(Department department);
    Department mapDepartmentDTOToDepartmentEntity(DepartmentDTO departmentDTO);
    void updateEntityFromDto(DepartmentDTO dto, @MappingTarget Department entity);


}

