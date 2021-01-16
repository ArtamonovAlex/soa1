package com.sussia.soa1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Soa1Application {

    public static void main(String[] args) {
        SpringApplication.run(Soa1Application.class, args);
    }

}
