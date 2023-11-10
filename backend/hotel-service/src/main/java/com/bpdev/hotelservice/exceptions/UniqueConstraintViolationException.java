package com.bpdev.hotelservice.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
