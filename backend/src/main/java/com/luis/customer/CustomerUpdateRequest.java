package com.luis.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {

}
