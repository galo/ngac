package dev.bromo.ngac;

import dev.bromo.ngac.policy.Policy;
import gov.nist.csd.pm.exceptions.PMException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        try {
            Policy p = Policy.Builder.build();
        } catch (PMException e) {
            e.printStackTrace();
        }

        SpringApplication.run(Application.class, args);
    }
}