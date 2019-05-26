package com.fpt.edu.app;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.fpt.edu.entities")
@EnableJpaRepositories("com.fpt.edu.repository")
//@EnableAsync
//@EnableAuthModule
//@EnableApiCaller
@ImportResource({"classpath:META-INF/application-config.xml"})
// scan for controller, services repository is from
public class Application {

    private static int AJP_PORT = 8609;
    private static int REDIRECT_PORT = 8443;

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
