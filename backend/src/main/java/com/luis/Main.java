package com.luis;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.luis.customer.Customer;
import com.luis.customer.CustomerRepository;
import com.luis.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //testing CI-Workflow
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository , PasswordEncoder passwordEncoder){
        return args -> {

            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            int age = random.nextInt(16, 80);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@amigoscode.com";
            Customer customer = new Customer(
                    firstName +  " " + lastName,
                    email,
                    passwordEncoder.encode("password"), age,
                    gender);
            customerRepository.save(customer);
            System.out.println(email);
        };
    }

}
