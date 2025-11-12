package com.example.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EmployeeResponseDTO {
    private Integer id;
    private String name;
    private Double salary;

    private String departmentName;
    private String projectName;
}
