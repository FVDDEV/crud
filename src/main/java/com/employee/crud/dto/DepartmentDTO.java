package com.employee.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDTO {
    private Integer did;

    @NotBlank(message = "Department name cannot be empty")
    @Size(min = 2, max = 20, message = "Department name must be between 2 to 20 characters")
    private String dname;
}
