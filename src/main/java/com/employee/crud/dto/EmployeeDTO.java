package com.employee.crud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
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
public class EmployeeDTO {

    @Schema(description = "Employee ID", example = "1")
    private Integer eid;
    @Schema(description = "First name of employee", example = "Falguni")
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 3, max = 20, message = "First name must be between 3 to 20 characters")
    private String fname;
    @Schema(description = "Last name of employee", example = "dattani")
    private String lname;
    @Schema(description = "Employee email", example = "fd@company.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true, nullable = false)
    private String email;

    private Integer did;

}
