package com.employee.crud.controller;

import com.employee.crud.dto.EmployeeDTO;
import com.employee.crud.entity.Employee;
import com.employee.crud.service.EmployeeService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/employees")
@Tag(name = "Employee Controller", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    @Operation(summary = "Get all employee ")
    @GetMapping()
    public ResponseEntity<List<EmployeeDTO>> getEmployees()
    {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployees(@PathVariable("id") Integer id)
    {
        return ResponseEntity.ok(employeeService.getEmployeesById(id));
    }

    @GetMapping("/employee")
    public Page<Employee> getEmployees(@RequestParam(value = "offset", required = false) Integer offset,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                               @RequestParam(value = "sortBy", required = false) String sortBy) {
        if(null == offset) offset = 0;
        if(null == pageSize) pageSize = 10;
        if(StringUtils.isEmpty(sortBy)) sortBy ="uid";
        return employeeService.getEmployeesPage(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO)
    {
        employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeeDTO employeeDTO)
    {
        employeeDTO.setEid(id);
        return ResponseEntity.ok(employeeService.updateEmployee(employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/department-id/{did}")
    public ResponseEntity<List<EmployeeDTO>> getDepartmentWiseEmployee(@PathVariable("did") Integer did)
    {
        return ResponseEntity.ok(employeeService.getDepartmentWiseEmployee(did));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

}
