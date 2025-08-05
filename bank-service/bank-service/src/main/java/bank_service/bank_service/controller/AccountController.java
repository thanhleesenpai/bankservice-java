package bank_service.bank_service.controller;

import bank_service.bank_service.model.Account;
import bank_service.bank_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Account create(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Optional<Account> get(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    public Account update(@PathVariable String id, @RequestBody Account updated) {
        return accountService.updateAccount(id, updated.getEmail(), updated.getPhoneNumber());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        accountService.deleteAccount(id);
    }
}

