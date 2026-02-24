package com.employee.crud.service.impl;


import com.employee.crud.dto.DepartmentDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.exception.DepartmentNotFoundException;
import com.employee.crud.exception.DuplicateDepartmentException;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.mapper.UserMapper;

import com.employee.crud.repository.DepartmentRepository;
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
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;

    @Test
    void getDepartmentsTest() {

        Department dept1 = new Department(1L, "IT");
        Department dept2 = new Department(2L, "HR");

        DepartmentDTO dto1 = new DepartmentDTO();
        dto1.setDid(Math.toIntExact(dept1.getDid()));
        dto1.setDname(dept1.getDname());
        DepartmentDTO dto2 = new DepartmentDTO(2, "HR");

        given(departmentRepository.findAll())
                .willReturn(List.of(dept1, dept2));

        given(userMapper.mapDepartmentEntityToDepartmentDTO(dept1)).willReturn(dto1);
        given(userMapper.mapDepartmentEntityToDepartmentDTO(dept2)).willReturn(dto2);

        List<DepartmentDTO> result = departmentServiceImpl.getDepartments();

        assertThat(result)
                .hasSize(2)
                .extracting(DepartmentDTO::getDname)
                .containsExactly("IT", "HR");

        verify(departmentRepository).findAll();
    }

    @Test
    void getDepartmentsThrowException_whenEmpty() {

        given(departmentRepository.findAll())
                .willReturn(List.of());

        assertThatThrownBy(() -> departmentServiceImpl.getDepartments())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("No departments found.");

        verify(departmentRepository).findAll();
        verifyNoInteractions(userMapper);
    }
    @Test
    void getDepartmentsByIdTest() {

        Department department = new Department(1L, "IT");
        DepartmentDTO dto = new DepartmentDTO(1, "IT");

        given(departmentRepository.findById(1L))
                .willReturn(Optional.of(department));

        given(userMapper.mapDepartmentEntityToDepartmentDTO(department))
                .willReturn(dto);

        DepartmentDTO result = departmentServiceImpl.getDepartmentsById(1);

        assertThat(result.getDid()).isEqualTo(1);

        verify(departmentRepository).findById(1L);
    }

    @Test
    void getDepartmentsByIdThrowException_whenNotFound() {

        given(departmentRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> departmentServiceImpl.getDepartmentsById(1))
                .isInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department not found.");

        verify(departmentRepository).findById(1L);
    }
    @Test
    void createDepartmentTest() {

        DepartmentDTO dto = new DepartmentDTO(1, "IT");
        Department entity = new Department(1L, "IT");

        given(departmentRepository.existsByDname("IT"))
                .willReturn(false);

        given(userMapper.mapDepartmentDTOToDepartmentEntity(dto))
                .willReturn(entity);

        departmentServiceImpl.createDepartment(dto);

        verify(departmentRepository).existsByDname("IT");
        verify(userMapper).mapDepartmentDTOToDepartmentEntity(dto);
        verify(departmentRepository).save(entity);
    }
    @Test
    void createDepartmentThrowException_whenDuplicate() {

        DepartmentDTO dto = new DepartmentDTO(1, "IT");

        given(departmentRepository.existsByDname("IT"))
                .willReturn(true);

        assertThatThrownBy(() -> departmentServiceImpl.createDepartment(dto))
                .isInstanceOf(DuplicateDepartmentException.class)
                .hasMessage("Department name already exist.");

        verify(departmentRepository).existsByDname("IT");
        verifyNoMoreInteractions(departmentRepository);
        verifyNoInteractions(userMapper);
    }
    @Test
    void updateDepartmentTest() {

        DepartmentDTO dto = new DepartmentDTO(1, "IT Updated");
        Department existing = new Department(1L, "IT");

        given(departmentRepository.findById(1L))
                .willReturn(Optional.of(existing));

        DepartmentDTO result = departmentServiceImpl.updateDepartment(dto);

        verify(userMapper).updateEntityFromDto(dto, existing);
        verify(departmentRepository).save(existing);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void updateDepartment_shouldThrowException_whenNotFound() {

        DepartmentDTO dto = new DepartmentDTO(1, "IT");

        given(departmentRepository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> departmentServiceImpl.updateDepartment(dto))
                .isInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department not found");
    }

    @Test
    void deleteDepartmentTest() {

        given(departmentRepository.existsById(1L)).willReturn(true);

        departmentServiceImpl.deleteDepartment(1L);

        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void deleteDepartmentThrowException_whenNotFound() {

        given(departmentRepository.existsById(1L)).willReturn(false);

        assertThatThrownBy(() -> departmentServiceImpl.deleteDepartment(1L))
                .isInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department not found");

        verify(departmentRepository, never()).deleteById(any());
    }

@Test
void getDepartmentsPageTest() {

    PageRequest request = PageRequest.of(0, 5);
    Page<Department> page =
            new PageImpl<>(List.of(new Department(1L, "IT")));

    given(departmentRepository.findAll(request))
            .willReturn(page);

    Page<Department> result =
            departmentServiceImpl.getDepartmentsPage(request);

    assertThat(result.getContent()).hasSize(1);

    verify(departmentRepository).findAll(request);
}




}

