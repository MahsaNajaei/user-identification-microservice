package com.energymeter.useridentificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserIdentificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserIdentificationServiceApplication.class, args);
    }

}
