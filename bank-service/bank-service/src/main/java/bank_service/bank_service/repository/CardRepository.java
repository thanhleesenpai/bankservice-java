package bank_service.bank_service.repository;

import bank_service.bank_service.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountId(String accountId);
}
