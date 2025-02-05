package pl.jania1857.fmsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FmsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmsApiApplication.class, args);
    }

}
