package bank_service.bank_service.service;

import bank_service.bank_service.dto.CardFullInfoDTO;
import bank_service.bank_service.dto.CardWithUsernameDTO;
import bank_service.bank_service.exception.AppException;
import bank_service.bank_service.model.Account;
import bank_service.bank_service.model.Balance;
import bank_service.bank_service.model.Card;
import bank_service.bank_service.repository.AccountRepository;
import bank_service.bank_service.repository.BalanceRepository;
import bank_service.bank_service.repository.CardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_CARDS_BY_ACCOUNT = "CARDS:ACCOUNT:";
    private static final String KEY_CARD_FULLINFO = "CARD:FULLINFO:";
    private static final String CACHE_CARDS_WITH_USERNAME = "CARDS:WITH_USERNAME";
    private static final String CACHE_CARDS_FULL_INFO = "CARDS:FULL_INFO";


    public List<Card> getCardsByAccountId(String accountId) {
        String redisKey = KEY_CARDS_BY_ACCOUNT + accountId;

        // 1. Check Redis
        List<Card> cachedCards = (List<Card>) redisTemplate.opsForValue().get(redisKey);
        if (cachedCards != null) {
            System.out.println("Redis HIT -> getCardsByAccountId({})" + accountId);
            return cachedCards;
        }

        // 2. Query DB
        System.out.println("Redis MISS -> getCardsByAccountId({})" + accountId);
        List<Card> cards = cardRepository.findByAccountId(accountId);

        // 3. Cache vào Redis (TTL 10 phút)
        redisTemplate.opsForValue().set(redisKey, cards, 10, TimeUnit.MINUTES);

        return cards;
    }
    public List<CardWithUsernameDTO> getAllCardsWithUsername() {
        // Kiểm tra cache
        List<CardWithUsernameDTO> cachedData =
                (List<CardWithUsernameDTO>) redisTemplate.opsForValue().get(CACHE_CARDS_WITH_USERNAME);

        if (cachedData != null) {
            System.out.println("Redis HIT: getAllCardsWithUsername()");
            return cachedData;
        }

        System.out.println("Redis MISS: getAllCardsWithUsername() - Query DB");
        List<Card> cards = cardRepository.findAll();

        List<CardWithUsernameDTO> result = cardRepository.findAllCardsWithUsername();

        // Lưu cache TTL 5 phút
        redisTemplate.opsForValue().set(CACHE_CARDS_WITH_USERNAME, result, 5, TimeUnit.MINUTES);

        return result;
    }

    public List<CardFullInfoDTO> getAllCardsFullInfo() {
        List<CardFullInfoDTO> cachedData =
                (List<CardFullInfoDTO>) redisTemplate.opsForValue().get(CACHE_CARDS_FULL_INFO);

        if (cachedData != null) {
            System.out.println("Redis HIT: getAllCardsFullInfo()");
            return cachedData;
        }

        System.out.println("Redis MISS: getAllCardsFullInfo() - Query DB");
        List<Card> cards = cardRepository.findAll();

        List<CardFullInfoDTO> result = cards.stream().map(card -> {
            Account account = accountRepository.findById(card.getAccountId()).orElse(null);
            Balance balance = balanceRepository.findById(card.getAccountId()).orElse(null);

            return new CardFullInfoDTO(
                    card.getCardId(),
                    card.getAccountId(),
                    account != null ? account.getCustomerName() : null,
                    account != null ? account.getEmail() : null,
                    account != null ? account.getPhoneNumber() : null,
                    card.getCardType(),
                    card.getExpiryDate(),
                    card.getStatus(),
                    balance != null ? balance.getAvailableBalance() : null,
                    balance != null ? balance.getHoldBalance() : null
            );
        }).toList();

        redisTemplate.opsForValue().set(CACHE_CARDS_FULL_INFO, result, 10, TimeUnit.MINUTES);

        return result;
    }
    public CardFullInfoDTO getCardFullInfoById(Long cardId) {
        String redisKey = KEY_CARD_FULLINFO + cardId;

// 1. Check Redis
        Object cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            CardFullInfoDTO cachedCard = mapper.convertValue(cached, CardFullInfoDTO.class);

            System.out.println("Redis HIT -> getCardFullInfoById(" + cardId + ")");
            return cachedCard;
        }

        // 2. Query DB
        System.out.println("Redis MISS -> getCardFullInfoById({})" + cardId);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        Account account = accountRepository.findById(card.getAccountId())
                .orElse(null);

        Balance balance = balanceRepository.findById(card.getAccountId())
                .orElse(null);

        CardFullInfoDTO dto = new CardFullInfoDTO(
                card.getCardId(),
                card.getAccountId(),
                account != null ? account.getCustomerName() : null,
                account != null ? account.getEmail() : null,
                account != null ? account.getPhoneNumber() : null,
                card.getCardType(),
                card.getExpiryDate(),
                card.getStatus(),
                balance != null ? balance.getAvailableBalance() : null,
                balance != null ? balance.getHoldBalance() : null
        );

        // 3. Lưu vào Redis
        redisTemplate.opsForValue().set(redisKey, dto, 10, TimeUnit.MINUTES);

        return dto;
    }



    public Optional<Card> createCard(Card card) {
        Optional<Account> account = accountRepository.findById(card.getAccountId());
        if (account.isEmpty()) return Optional.empty();

        // Nếu chưa set ngày hết hạn, tự động set 5 năm sau
        if (card.getExpiryDate() == null) {
            card.setExpiryDate(LocalDate.now().plusYears(5));
        }
        card.setStatus("active");

        Card saved = cardRepository.save(card);

        clearCardCache(card.getAccountId(), card.getCardId());

        return Optional.of(saved);
    }
    public Card updateStatus(Long cardId, String status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new AppException("Card not found"));

        card.setStatus(status);

        Card updated = cardRepository.save(card);

        clearCardCache(card.getAccountId(), cardId);

        return updated;
    }

    public boolean deleteCard(Long cardId) {
        Optional<Card> cardOpt = cardRepository.findById(cardId);
        if (cardOpt.isEmpty()) return false;

        Card card = cardOpt.get();
        if (!cardRepository.existsById(cardId)) return false;

        cardRepository.deleteById(cardId);
        clearCardCache(card.getAccountId(), cardId);

        System.out.println("Cache cleared: after deleteCard()");
        return true;
    }
    private void clearCardCache(String accountId, Long cardId) {
        redisTemplate.delete(KEY_CARDS_BY_ACCOUNT + accountId);
        redisTemplate.delete(KEY_CARD_FULLINFO + cardId);
        redisTemplate.delete(CACHE_CARDS_WITH_USERNAME);
        redisTemplate.delete(CACHE_CARDS_FULL_INFO);
    }

}
