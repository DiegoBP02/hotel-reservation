package com.bpdev.reservationservice.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
