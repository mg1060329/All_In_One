package com.mongoDB.Liquibase_app.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper getModeMapper(){
        return new ModelMapper();
    }

}
