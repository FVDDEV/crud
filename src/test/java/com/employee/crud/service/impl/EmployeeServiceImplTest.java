package com.employee.crud.service.impl;

import com.employee.crud.dto.EmployeeDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.entity.Employee;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.mapper.UserMapper;
import com.employee.crud.repository.EmployeeRepository;
import com.employee.crud.service.EmployeeService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest
{
    @Mock
    EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    
    @Mock
    UserMapper userMapper;

    @InjectMocks
    EmployeeServiceImpl employeeServiceImpl;

    @Test
    void getEmployeesTest() {

        Employee emp1 = new Employee(1, "falguni","dattani","fd@gmail.com",null);
        Employee emp2 = new Employee(2, "vinee","chaudhary", "vc@gmail.com",null);

        EmployeeDTO dto1 = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);
        EmployeeDTO dto2 = new EmployeeDTO(2, "vinee","chaudhary", "vc@gmail.com",1);

        given(employeeRepository.findAll())
                .willReturn(List.of(emp1, emp2));

        given(userMapper.mapEmployeeEntityToEmployeeDTO(emp1)).willReturn(dto1);
        given(userMapper.mapEmployeeEntityToEmployeeDTO(emp2)).willReturn(dto2);

        List<EmployeeDTO> result = employeeServiceImpl.getEmployees();

        assertThat(result).hasSize(2);

        verify(employeeRepository).findAll();
    }
    @Test
    void getEmployeesThrowException_whenEmpty() {

        given(employeeRepository.findAll())
                .willReturn(List.of());

        assertThatThrownBy(() -> employeeServiceImpl.getEmployees())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("No Employees found.");

        verify(employeeRepository).findAll();
        verifyNoInteractions(userMapper);
    }
    @Test
    void getEmployeesByIdTest() {

        Employee emp = new Employee(1, "falguni","dattani","fd@gmail.com",null);
        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);

        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(emp));

        given(userMapper.mapEmployeeEntityToEmployeeDTO(emp))
                .willReturn(dto);

        EmployeeDTO result = employeeServiceImpl.getEmployeesById(1);

        assertThat(result.getEid()).isEqualTo(1);

        verify(employeeRepository).findById(1L);
    }
    @Test
    void getEmployeesByIdThrowException_whenNotFound() {

        given(employeeRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> employeeServiceImpl.getEmployeesById(1))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Employee not found.");

        verify(employeeRepository).findById(1L);
    }
    @Test
    void createEmployeeTest() {

        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);
        Employee emp = new Employee(1, "falguni","dattani","fd@gmail.com",new Department(1L,"HR"));

        given(userMapper.mapEmployeeDTOToEmployeeEntity(dto))
                .willReturn(emp);

        employeeServiceImpl.createEmployee(dto);

        verify(userMapper).mapEmployeeDTOToEmployeeEntity(dto);
        verify(employeeRepository).save(emp);
    }
    @Test
    void updateEmployeeUpdateAndReturnDTO() {

        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);
        dto.setDid(10);

        Employee existing = new Employee(1, "falguniv","dattani","fvd@gmail.com",new Department(1L,"HR"));

        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(existing));

        employeeServiceImpl.updateEmployee(dto);

        verify(userMapper).updateEntityFromDto(dto, existing);
        verify(employeeRepository).save(existing);

        assertThat(existing.getDepartment()).isNotNull();
        assertThat(existing.getDepartment().getDid()).isEqualTo(10L);
    }
    @Test
    void updateEmployeeThrowException_whenNotFound() {

        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);

        given(employeeRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> employeeServiceImpl.updateEmployee(dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Employee not found");
    }
    @Test
    void deleteEmployeeTest() {

        given(employeeRepository.existsById(1L)).willReturn(true);

        employeeServiceImpl.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployeeThrowException_whenNotFound()
    {
        given(employeeRepository.existsById(1L)).willReturn(false);
        assertThatThrownBy(() -> employeeServiceImpl.deleteEmployee(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Employee not found");
        verify(employeeRepository,never()).deleteById(any());
    }

    @Test
    void getEmployeesPageTest() {

        PageRequest request = PageRequest.of(0, 5);
        Page<Employee> page =
                new PageImpl<>(List.of(new Employee(1, "falguniv","dattani","fvd@gmail.com",new Department(1L,"HR"))));

        given(employeeRepository.findAll(request))
                .willReturn(page);

        Page<Employee> result = employeeServiceImpl.getEmployeesPage(request);

        assertThat(result.getContent()).hasSize(1);

        verify(employeeRepository).findAll(request);
    }
    @Test
    void getDepartmentWiseEmployeeTest() {

        Employee emp = new Employee(1, "falguni","dattani","fd@gmail.com",new Department(1L,"HR"));
        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);

        given(employeeRepository.findByDepartmentDid(10))
                .willReturn(List.of(emp));

        given(userMapper.mapEmployeeEntityToEmployeeDTO(emp))
                .willReturn(dto);

        List<EmployeeDTO> result =
                employeeServiceImpl.getDepartmentWiseEmployee(10);

        assertThat(result).hasSize(1);
    }
    @Test
    void getDepartmentWiseEmployeeThrowException_whenEmpty() {

        given(employeeRepository.findByDepartmentDid(10))
                .willReturn(List.of());

        assertThatThrownBy(() ->
                employeeServiceImpl.getDepartmentWiseEmployee(10))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("No Employee Found Under this department");
    }
    @Test
    void getEmployeeByEmailTest() {

        Employee emp = new Employee(1, "falguni","dattani","fd@gmail.com",new Department(1L,"HR"));
        EmployeeDTO dto = new EmployeeDTO(1, "falguni","dattani","fd@gmail.com",1);

        given(employeeRepository.findByEmail("fd@gmail.com"))
                .willReturn(Optional.of(emp));

        given(userMapper.mapEmployeeEntityToEmployeeDTO(emp))
                .willReturn(dto);

        EmployeeDTO result =
                employeeServiceImpl.getEmployeeByEmail("fd@gmail.com");

        assertThat(result.getEid()).isEqualTo(1);
    }
    @Test
    void getEmployeeByEmailThrowException_whenNotFound() {

        given(employeeRepository.findByEmail("fvd@gmail.com"))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
                employeeServiceImpl.getEmployeeByEmail("fvd@gmail.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Employee not found with email");
    }




}
