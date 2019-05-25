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

    private static int AJP_PORT = 8609;
    private static int REDIRECT_PORT = 8443;

    public static void main(String[] args){
        if(args.length > 0){
            Integer ajpPort = Integer.valueOf(args[0]);
            AJP_PORT = ajpPort;
        }
        SpringApplication.run(Application.class, args);
    }
}
