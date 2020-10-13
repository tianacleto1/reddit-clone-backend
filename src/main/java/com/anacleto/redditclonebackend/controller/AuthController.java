package com.anacleto.redditclonebackend.controller;

import com.anacleto.redditclonebackend.model.dto.AuthenticationResponseDTO;
import com.anacleto.redditclonebackend.model.dto.LoginRequestDTO;
import com.anacleto.redditclonebackend.model.dto.RegisterRequestDTO;
import com.anacleto.redditclonebackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequestDTO registerRequestDto) {
        authService.signup(registerRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);

        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Its working fine", HttpStatus.OK);
    }
}
