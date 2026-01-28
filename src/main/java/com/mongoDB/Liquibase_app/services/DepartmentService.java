package com.mongoDB.Liquibase_app.services;

import com.mongoDB.Liquibase_app.dto.DepartmentRequestDto;
import com.mongoDB.Liquibase_app.dto.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {

    // CREATE
    DepartmentResponseDto createDepartment(DepartmentRequestDto dto);

    // READ ALL
    List<DepartmentResponseDto> getAllDepartments();

    // READ BY ID
    DepartmentResponseDto getDepartmentById(Long id);

    // UPDATE
    DepartmentResponseDto updateDepartment(Long id,
                                           DepartmentRequestDto dto);

    // DELETE
    void deleteDepartment(Long id);
}
