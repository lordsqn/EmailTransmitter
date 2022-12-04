package pl.lssystems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailTransmitter {

    public static void main(String ... args) {
        SpringApplication.run(EmailTransmitter.class, args);
    }

}
