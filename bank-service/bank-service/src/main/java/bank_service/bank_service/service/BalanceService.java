package bank_service.bank_service.service;

import bank_service.bank_service.model.Balance;
import bank_service.bank_service.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public Optional<Balance> getBalance(String accountId) {
        return balanceRepository.findById(accountId);
    }

            public Balance deposit(String accountId, BigDecimal amount) {
        Balance balance = balanceRepository.findById(accountId)
                .orElse(Balance.builder()
                        .accountId(accountId)
                        .availableBalance(BigDecimal.ZERO)
                        .holdBalance(BigDecimal.ZERO)
                        .build());

        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
        return balanceRepository.save(balance);
    }

    public Balance withdraw(String accountId, BigDecimal amount) {
        Balance balance = balanceRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient available balance");
        }

        balance.setAvailableBalance(balance.getAvailableBalance().subtract(amount));
        return balanceRepository.save(balance);
    }
}
