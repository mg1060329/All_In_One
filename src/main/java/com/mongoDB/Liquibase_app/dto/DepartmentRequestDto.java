package com.mongoDB.Liquibase_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDto {

    @Id
    private Long id;
    @NotBlank
    private String name;
    private String description;

}
