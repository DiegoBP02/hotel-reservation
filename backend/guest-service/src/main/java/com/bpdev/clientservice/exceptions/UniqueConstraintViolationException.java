package com.bpdev.clientservice.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
