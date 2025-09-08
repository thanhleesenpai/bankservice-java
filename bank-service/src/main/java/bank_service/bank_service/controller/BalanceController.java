package bank_service.bank_service.controller;

import bank_service.bank_service.model.Balance;
import bank_service.bank_service.service.BalanceService;
import lombok.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public ResponseEntity<Balance> getBalance() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountId = userDetails.getUsername();

        return balanceService.getBalance(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//
//    @PutMapping("/deposit/{accountId}")
//    public ResponseEntity<?> deposit(
//            @PathVariable String accountId,
//            @RequestBody BalanceRequest request,
//            Authentication authentication) {
//
//        String requesterId = authentication.getName(); // accountId từ JWT
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//
//        if (!isAdmin && !requesterId.equals(accountId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You are not allowed to deposit to this account.");
//        }
//
//        try {
//            return ResponseEntity.ok(balanceService.deposit(accountId, request.getAmount()));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/withdraw/{accountId}")
//    public ResponseEntity<?> withdraw(
//            @PathVariable String accountId,
//            @RequestBody BalanceRequest request,
//            Authentication authentication) {
//
//        String requesterId = authentication.getName();
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//
//        if (!isAdmin && !requesterId.equals(accountId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You are not allowed to withdraw from this account.");
//        }
//
//        try {
//            return ResponseEntity.ok(balanceService.withdraw(accountId, request.getAmount()));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//
//    @Data
//    static class BalanceRequest {
//        private Long accountId;
//        private BigDecimal amount;
//    }
}

