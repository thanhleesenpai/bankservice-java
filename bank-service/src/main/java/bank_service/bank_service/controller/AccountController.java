package bank_service.bank_service.controller;

import bank_service.bank_service.exception.AppException;
import bank_service.bank_service.model.Account;
import bank_service.bank_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Account create(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id, Authentication authentication) {
        String requesterId = authentication.getName(); // Lấy accountId từ JWT
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !requesterId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to view this account.");
        }

        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{id}")
    public Account update(@PathVariable String id, @RequestBody Account updated) {
        return accountService.updateAccount(id, updated.getCustomerName(), updated.getEmail(), updated.getPhoneNumber(), updated.getRole());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable String id, Authentication authentication) {

        String requesterId = authentication.getName(); // accountId từ JWT
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !requesterId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to delete this account.");
        }

        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error occurred.");
        }
    }

}

