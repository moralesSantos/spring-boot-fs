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
import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //testing CI-Workflow
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {

            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            int age = random.nextInt(16, 80);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            Customer customer = new Customer(
                    firstName +  " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@amigoscode.com",
                    age,
                    gender);
            customerRepository.save(customer);
        };
    }

}
