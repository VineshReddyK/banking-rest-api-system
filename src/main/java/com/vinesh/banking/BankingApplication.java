package com.vinesh.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// entry point — Spring Boot auto-configures everything from
// application.properties and the component scan picks up all
// services, controllers, and repositories in this package.
@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
}
