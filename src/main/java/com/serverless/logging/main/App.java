package com.serverless.logging.main;


import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "com.serverless", excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.serverless.logging.advice.*"))
@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}