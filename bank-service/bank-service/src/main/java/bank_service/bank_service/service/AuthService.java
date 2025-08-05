package bank_service.bank_service.service;

import bank_service.bank_service.dto.AuthResponse;
import bank_service.bank_service.dto.LoginRequest;
import bank_service.bank_service.model.Account;
import bank_service.bank_service.dto.RegisterRequest;
import bank_service.bank_service.repository.AccountRepository;
import bank_service.bank_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        if (accountRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã được sử dụng");
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .customerName(request.getCustomerName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        accountRepository.save(account);

        String token = jwtService.generateToken(account.getAccountId());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(account.getAccountId());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
