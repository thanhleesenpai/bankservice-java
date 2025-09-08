package bank_service.bank_service.repository;

import bank_service.bank_service.model.Transaction;
import bank_service.bank_service.model.TransactionStatus;
import bank_service.bank_service.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    // Tìm giao dịch theo mã xác thực (dùng để verify email code)
    Optional<Transaction> findByVerificationCode(String verificationCode);

    // Lấy danh sách giao dịch theo trạng thái (VD: admin duyệt các giao dịch PENDING)
    //List<Transaction> findByStatus(TransactionStatus status);
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);

    // Lấy tất cả giao dịch theo accountId (tài khoản nguồn hoặc đích)
    List<Transaction> findByFromAccountIdOrToAccountId(String fromAccountId, String toAccountId);

    // Phương thức tìm kiếm tất cả giao dịch có phân trang
    Page<Transaction> findAll(Pageable pageable);

    // Phương thức tìm kiếm có lọc theo loại và trạng thái
    Page<Transaction> findByTransactionTypeAndStatus(TransactionType transactionType, TransactionStatus status, Pageable pageable);

    Page<Transaction> findByTransactionType(TransactionType transactionType, Pageable pageable);
}
