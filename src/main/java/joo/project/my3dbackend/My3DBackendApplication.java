package joo.project.my3dbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class My3DBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(My3DBackendApplication.class, args);
    }
}
