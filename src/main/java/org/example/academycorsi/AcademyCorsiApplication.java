package org.example.academycorsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class AcademyCorsiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademyCorsiApplication.class, args);
    }

}
