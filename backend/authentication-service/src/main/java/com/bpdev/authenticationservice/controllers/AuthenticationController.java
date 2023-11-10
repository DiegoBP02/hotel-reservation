package com.bpdev.authenticationservice.controllers;

import com.bpdev.authenticationservice.dtos.LoginRequest;
import com.bpdev.authenticationservice.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest);
        return ResponseEntity.ok().body(token);
    }

}