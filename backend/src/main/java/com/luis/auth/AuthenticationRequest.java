package com.luis.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
