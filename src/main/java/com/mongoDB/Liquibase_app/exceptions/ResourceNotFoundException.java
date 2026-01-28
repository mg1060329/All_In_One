package com.mongoDB.Liquibase_app.exceptions;



public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException(String message) {

        super(message);
    }
}
