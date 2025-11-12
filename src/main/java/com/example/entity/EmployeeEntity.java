package com.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EmployeeEntity {
    private Integer id;
    private String name;
    private Double salary;
    private Integer departmentId;
    private Integer projectId;

    // joined fields (for response)
    private String departmentName;
    private String projectName;
}