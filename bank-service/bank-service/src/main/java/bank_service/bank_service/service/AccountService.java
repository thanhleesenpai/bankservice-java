package bank_service.bank_service.service;

import bank_service.bank_service.model.Account;
import bank_service.bank_service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    public Account updateAccount(String accountId, String email, String phoneNumber) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setEmail(email);
        acc.setPhoneNumber(phoneNumber);
        return accountRepository.save(acc);
    }

    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }
}

