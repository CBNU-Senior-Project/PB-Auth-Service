package com.phishing.authservice.controller;

import com.phishing.authservice.dto.request.SignInRequest;
import com.phishing.authservice.dto.request.SignUpRequest;
import com.phishing.authservice.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @GetMapping("/users/check")
    public ResponseEntity<?> checkEmail(@RequestParam @Valid @NotNull String email) {
        authService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }
}