
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan ;


@SpringBootApplication
@ComponentScan({"com.example.demo.controller","com.example.demo.service"}) // it takes all by default ..
public class App {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
