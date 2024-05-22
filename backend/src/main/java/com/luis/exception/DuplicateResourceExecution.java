package com.luis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceExecution extends RuntimeException {
    public DuplicateResourceExecution(String message) {
        super(message);
    }

}
