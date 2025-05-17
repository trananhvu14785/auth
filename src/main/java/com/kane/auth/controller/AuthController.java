package com.kane.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/auth")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok("This is auth service");
    }
}
