package com.bpdev.roomservice.exceptions;

import java.util.UUID;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String msg) {
        super(msg);
    }
}
