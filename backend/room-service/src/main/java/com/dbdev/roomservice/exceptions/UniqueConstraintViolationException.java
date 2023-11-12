package com.dbdev.roomservice.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String msg) {
        super(msg);
    }
}
