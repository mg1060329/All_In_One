package com.mongoDB.Liquibase_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {

    @Id
    private Long id;
    private String name;
    private String description;

}
