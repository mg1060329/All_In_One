package com.mongoDB.Liquibase_app.services.impl;

import com.mongoDB.Liquibase_app.dto.DepartmentRequestDto;
import com.mongoDB.Liquibase_app.dto.DepartmentResponseDto;
import com.mongoDB.Liquibase_app.entities.Department;
import com.mongoDB.Liquibase_app.repositories.DepartmentRepository;
import com.mongoDB.Liquibase_app.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;


    // CREATE
    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {
        Department department = modelMapper.map(dto, Department.class);
        Department saved = departmentRepository.save(department);
        return modelMapper.map(saved, DepartmentResponseDto.class);
    }

    // GET ALL
    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(dept -> modelMapper.map(dept, DepartmentResponseDto.class))
                .collect(Collectors.toList());
    }

    // GET BY ID
    @Override
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with id: " + id));

        return modelMapper.map(department, DepartmentResponseDto.class);
    }

    // UPDATE
    @Override
    public DepartmentResponseDto updateDepartment(Long id,
                                                  DepartmentRequestDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Department updated = departmentRepository.save(department);
        return modelMapper.map(updated, DepartmentResponseDto.class);
    }

    // DELETE
    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
