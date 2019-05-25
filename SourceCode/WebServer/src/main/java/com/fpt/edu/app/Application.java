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
@EnableJpaRepositories("com.fptedu.web.repository")
//@EnableAsync
//@EnableAuthModule
//@EnableApiCaller
@ImportResource({"classpath:META-INF/application-config.xml"})
// scan for controller, services repository is from
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

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("AJP/1.3");
        connector.setScheme("http");
        connector.setPort(AJP_PORT);
        connector.setRedirectPort(REDIRECT_PORT);
        connector.setSecure(false);
        connector.setAllowTrace(false);
        return connector;
    }

}
