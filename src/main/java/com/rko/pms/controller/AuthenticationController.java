package com.rko.pms.controller;

import com.rko.pms.ValidationUtil;
import com.rko.pms.dto.AuthenticationRequestDTO;
import com.rko.pms.dto.RegisterRequestDTO;
import com.rko.pms.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = ValidationUtil.getErrorMessages(result);
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        service.register(request);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = ValidationUtil.getErrorMessages(result);
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(service.authenticate(request));
    }
}