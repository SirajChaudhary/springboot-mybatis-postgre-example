package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private Double salary;

    @NotNull
    private Integer departmentId;

    @NotNull
    private Integer projectId;
}
