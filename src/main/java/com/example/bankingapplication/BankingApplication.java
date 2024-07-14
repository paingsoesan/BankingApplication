package com.example.bankingapplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Peter's Bank App",
                description = "Backend Rest APIs for Peter's Bank",
                version = "v1.0",
                contact = @Contact(
                        name = "Paing Soe San",
                        email = "paingsoesan383@gmail.com",
                        url = "https://github.com/paingsoesan/BankingApplication"
                ),
                license = @License(
                        name = "Peter from JDC",
                        url = "https://github.com/paingsoesan/BankingApplication"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Bla Bla Bla",
                url = "https://github.com/paingsoesan/BankingApplication"
        )
)
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}
