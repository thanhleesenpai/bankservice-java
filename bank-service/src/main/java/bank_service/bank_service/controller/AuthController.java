package bank_service.bank_service.controller;

import bank_service.bank_service.dto.AuthResponse;
import bank_service.bank_service.dto.LoginRequest;
import bank_service.bank_service.dto.RegisterRequest;
import bank_service.bank_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}


