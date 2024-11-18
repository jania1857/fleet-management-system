package pl.jania1857.fms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FleetManagementSystemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetManagementSystemApiApplication.class, args);
    }

}
