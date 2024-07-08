package com.luis.journey;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.luis.auth.AuthenticationRequest;
import com.luis.auth.AuthenticationResponse;
import com.luis.customer.CustomerDTO;
import com.luis.customer.CustomerRegistrationRequest;
import com.luis.customer.Gender;
import com.luis.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationIT {

        @Autowired
        private WebTestClient webTestClient;
        @Autowired
        private JWTUtil jwtUtil;

        private static final Random RANDOM = new Random();
        private static final String AUTHENTICATION_PATH = "/api/v1/auth";
        private static final String CUSTOMER_PATH =  "/api/v1/customers";

        @Test
        void canLogin() {
                Faker faker = new Faker();
                Name fakerName = faker.name();
                String name = faker.name().fullName();
                String email = fakerName.lastName() + UUID.randomUUID() + "@amigoscode.com";
                int age = RANDOM.nextInt(1, 100);
                String password = "password";

                Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

                //Create Registration request
                CustomerRegistrationRequest  customerRegistrationRequest = new CustomerRegistrationRequest(
                        name, email, password, age, gender
                );
                AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                        email,
                        password
                );

                webTestClient.post()
                        .uri(AUTHENTICATION_PATH + "/login" )
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isUnauthorized();
                //send a post request
                webTestClient.post()
                        .uri(CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(
                                customerRegistrationRequest),
                                CustomerRegistrationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk();

                EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                        .uri(AUTHENTICATION_PATH + "/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                        })
                        .returnResult();

                String jwtToken = result
                        .getResponseHeaders()
                        .get(AUTHORIZATION)
                        .get(0);

                AuthenticationResponse authenticationResponse = result.getResponseBody();

                CustomerDTO customerDTO = authenticationResponse.customerDTO();

                assertThat(jwtUtil.isTokenValid(jwtToken,
                        customerDTO.username())).isTrue();

                assertThat(customerDTO.email()).isEqualTo(email);
                assertThat(customerDTO.age()).isEqualTo(age);
                assertThat(customerDTO.name()).isEqualTo(name);
                assertThat(customerDTO.username()).isEqualTo(email);
                assertThat(customerDTO.gender()).isEqualTo(gender);
                assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));

}

}
