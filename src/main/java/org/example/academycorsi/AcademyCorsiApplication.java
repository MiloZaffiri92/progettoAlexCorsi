package org.example.academycorsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AcademyCorsiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademyCorsiApplication.class, args);
    }

}
