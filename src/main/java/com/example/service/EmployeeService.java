package com.example.service;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import java.util.List;

public interface EmployeeService {

    // --- CRUD Operations ---
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);

    EmployeeResponseDTO getEmployeeById(Integer id);

    EmployeeResponseDTO updateEmployee(Integer id, EmployeeRequestDTO employeeRequestDTO);

    void deleteEmployee(Integer id);

    // --- Additional Operations ---
    List<EmployeeResponseDTO> getAllEmployees();

    List<EmployeeResponseDTO> searchEmployees(String search, String sortBy, String sortOrder, int page, int size);
}
