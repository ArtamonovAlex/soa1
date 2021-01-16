package com.sussia.soa1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@EntityScan(basePackages = {"com.sussia.soa1.model"})
@EnableJpaRepositories(basePackages = {"com.sussia.soa1.repositories"})
@SpringBootApplication
public class Soa1Application {

    public static void main(String[] args) {
        SpringApplication.run(Soa1Application.class, args);
    }

}
