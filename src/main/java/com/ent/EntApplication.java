package com.ent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EntApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntApplication.class, args);
    }

}
