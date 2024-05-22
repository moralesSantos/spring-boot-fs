package com.luis.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {

}
