package com.mongoDB.Liquibase_app.repositories;

import com.mongoDB.Liquibase_app.entities.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, Long> {
}
