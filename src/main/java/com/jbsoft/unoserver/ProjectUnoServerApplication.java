package com.jbsoft.unoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ProjectUnoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectUnoServerApplication.class, args);
    }

}
