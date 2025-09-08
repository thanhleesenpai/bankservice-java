package bank_service.bank_service.repository;

import bank_service.bank_service.model.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
    Page<TransactionHistory> findByFromAccountIdOrToAccountId(String fromAccountId, String toAccountId, Pageable pageable);
}