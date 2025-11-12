package com.example.service;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;
import com.example.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    // --- CRUD Operations ---

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity entity = toEntity(employeeRequestDTO);
        employeeMapper.insert(entity);
        return toResponseDTO(entity);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Integer id) {
        //EmployeeEntity entity = employeeMapper.findById(id);
        EmployeeEntity entity = employeeMapper.findByIdWithJoin(id);
        return toResponseDTO(entity);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Integer id, EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity entity = toEntity(employeeRequestDTO);
        entity.setId(id);
        employeeMapper.update(entity);
        return toResponseDTO(entity);
    }

    @Override
    public void deleteEmployee(Integer id) {
        employeeMapper.delete(id);
    }

    // --- Additional Operations ---

    /*
     * Retrieves all employees along with their department and project details.
     * Uses MyBatis JOIN query and maps each entity to a response DTO.
     */
    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeMapper.findAllWithDepartmentAndProject()
                .stream()
                .map(this::toResponseDTO)// Convert each entity to DTO
                .toList();
    }

    /*
     * Searches employees by name, department, or project with sorting and pagination.
     * Delegates to MyBatis dynamic SQL query and maps results to DTOs.
     */
    @Override
    public List<EmployeeResponseDTO> searchEmployees(String search, String sortBy, String sortOrder, int page, int size) {
        int offset = (page - 1) * size; // Number of records to skip before fetching the current page (used for SQL pagination)
        return employeeMapper.searchEmployees(search, sortBy, sortOrder, offset, size)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // --- Helper Methods ---

    /** Converts EmployeeRequestDTO to EmployeeEntity */
    private EmployeeEntity toEntity(EmployeeRequestDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(dto.getName());
        entity.setSalary(dto.getSalary());
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setProjectId(dto.getProjectId());
        return entity;
    }

    /** Converts EmployeeEntity to EmployeeResponseDTO */
    private EmployeeResponseDTO toResponseDTO(EmployeeEntity entity) {
        if (entity == null) return null;
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSalary(entity.getSalary());
        dto.setDepartmentName(entity.getDepartmentName());
        dto.setProjectName(entity.getProjectName());
        return dto;
    }
}
