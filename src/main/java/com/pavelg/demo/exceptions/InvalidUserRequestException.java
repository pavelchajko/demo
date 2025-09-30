package com.pavelg.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;


public class InvalidUserRequestException extends ResponseStatusException {

    public InvalidUserRequestException(String reason, HttpStatus status) {
        super(status, reason);
    }
}
