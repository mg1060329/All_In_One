package com.mongoDB.Liquibase_app.controllers;

import com.mongoDB.Liquibase_app.dto.DepartmentRequestDto;
import com.mongoDB.Liquibase_app.dto.DepartmentResponseDto;
import com.mongoDB.Liquibase_app.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    // CREATE
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody DepartmentRequestDto dto) {

        DepartmentResponseDto response =
                departmentService.createDepartment(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                departmentService.getDepartmentById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto dto) {

        return ResponseEntity.ok(
                departmentService.updateDepartment(id, dto)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
