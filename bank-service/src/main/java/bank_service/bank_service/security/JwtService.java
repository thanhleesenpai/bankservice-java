package bank_service.bank_service.security;

import bank_service.bank_service.model.Role;
import bank_service.bank_service.repository.AccountRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JwtService {

    private static final String SECRET_KEY = "01234567890123456789012345678901"; // 32 ký tự

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Autowired
    private AccountRepository accountRepository;
    public String generateToken(String accountId, Role role) {
        return Jwts.builder()
                .setSubject(accountId) // subject = accountId
                .claim("role", role.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 giờ
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractAccountId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Subject chứa accountId
    }
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }


}
