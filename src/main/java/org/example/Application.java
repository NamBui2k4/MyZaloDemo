package org.example; // phải là org.example, không phải org.example.backend hay gì khác

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {  // tên class phải là Application
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}