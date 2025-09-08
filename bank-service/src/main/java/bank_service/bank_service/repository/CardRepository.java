package bank_service.bank_service.repository;

import bank_service.bank_service.dto.CardWithUsernameDTO;
import bank_service.bank_service.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT new bank_service.bank_service.dto.CardWithUsernameDTO(c.cardId, c.accountId, a.customerName, c.cardType, c.expiryDate, c.status) " +
            "FROM Card c JOIN Account a ON c.accountId = a.accountId")
    List<CardWithUsernameDTO> findAllCardsWithUsername();
    List<Card> findByAccountId(String accountId);
    boolean existsByAccountIdAndStatus(String accountId, String status);
}
