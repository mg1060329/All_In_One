package com.mongoDB.Liquibase_app.service;

import com.mongoDB.Liquibase_app.dto.DepartmentRequestDto;
import com.mongoDB.Liquibase_app.dto.DepartmentResponseDto;
import com.mongoDB.Liquibase_app.entities.Department;
import com.mongoDB.Liquibase_app.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {
        Department department = modelMapper.map(dto, Department.class);
        Department saved = departmentRepository.save(department);
        return modelMapper.map(saved, DepartmentResponseDto.class);
    }

    // GET ALL
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(dept -> modelMapper.map(dept, DepartmentResponseDto.class))
                .collect(Collectors.toList());
    }

    // GET BY ID
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with id: " + id)
                );

        return modelMapper.map(department, DepartmentResponseDto.class);
    }

    // UPDATE
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with id: " + id)
                );

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Department updated = departmentRepository.save(department);
        return modelMapper.map(updated, DepartmentResponseDto.class);
    }

    // DELETE
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
