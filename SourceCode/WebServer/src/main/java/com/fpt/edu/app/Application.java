package com.fpt.edu.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.fpt.edu.entities")
@EnableJpaRepositories("com.fpt.edu.repository")
@ImportResource({"classpath:META-INF/application-config.xml"})

public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
