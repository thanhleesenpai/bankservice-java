package bank_service.bank_service.service;

import bank_service.bank_service.model.Account;
import bank_service.bank_service.model.Card;
import bank_service.bank_service.repository.AccountRepository;
import bank_service.bank_service.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public List<Card> getCardsByAccountId(String accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    public Optional<Card> createCard(Card card) {
        Optional<Account> account = accountRepository.findById(card.getAccountId());
        if (account.isEmpty()) return Optional.empty();

        // Nếu chưa set ngày hết hạn, tự động set 5 năm sau
        if (card.getExpiryDate() == null) {
            card.setExpiryDate(LocalDate.now().plusYears(5));
        }
        card.setStatus("active");

        return Optional.of(cardRepository.save(card));
    }

    public boolean deleteCard(Long cardId) {
        // Giả sử chưa có transaction nên cho xóa luôn
        if (!cardRepository.existsById(cardId)) return false;
        cardRepository.deleteById(cardId);
        return true;
    }
}
