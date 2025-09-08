package bank_service.bank_service.repository;
import bank_service.bank_service.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface BalanceRepository extends JpaRepository<Balance, String> {
    Optional<Balance> findByAccountId(String accountId);
}
