package bank_service.bank_service.controller;

import bank_service.bank_service.model.Balance;
import bank_service.bank_service.service.BalanceService;
import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public ResponseEntity<Balance> getBalance() {
        String accountId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return balanceService.getBalance(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/deposit")
    public ResponseEntity<Balance> deposit(@RequestBody BalanceRequest request) {
        String accountId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(balanceService.deposit(accountId, request.getAmount()));
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Balance> withdraw(@RequestBody BalanceRequest request) {
        String accountId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(balanceService.withdraw(accountId, request.getAmount()));
    }


    //    @GetMapping("/{accountId}")
//    public ResponseEntity<Balance> getBalance(@PathVariable Long accountId) {
//        return balanceService.getBalance(accountId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/deposit")
//    public ResponseEntity<Balance> deposit(@RequestBody BalanceRequest request) {
//        return ResponseEntity.ok(balanceService.deposit(request.getAccountId(), request.getAmount()));
//    }
//
//    @PutMapping("/withdraw")
//    public ResponseEntity<Balance> withdraw(@RequestBody BalanceRequest request) {
//        return ResponseEntity.ok(balanceService.withdraw(request.getAccountId(), request.getAmount()));
//    }
//
    @Data
    static class BalanceRequest {
        private Long accountId;
        private BigDecimal amount;
    }
}

