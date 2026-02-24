package com.employee.crud.controller;

import com.employee.crud.dto.DepartmentDTO;
import com.employee.crud.entity.Department;
import com.employee.crud.service.DepartmentService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping()
    public ResponseEntity<List<DepartmentDTO>> getDepartments()
    {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartments(@PathVariable("id") Integer id)
    {
        return ResponseEntity.ok(departmentService.getDepartmentsById(id));
    }

    @GetMapping("/department")
    public Page<Department> getDepartments(@RequestParam(value = "offset", required = false) Integer offset,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                               @RequestParam(value = "sortBy", required = false) String sortBy) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="uid";
        return departmentService.getDepartmentsPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO)
    {
        departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update-department/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable("id") Integer id, @Valid @RequestBody DepartmentDTO departmentDTO)
    {
        departmentDTO.setDid(id);
        return ResponseEntity.ok(departmentService.updateDepartment(departmentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
