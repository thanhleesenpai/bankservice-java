package bank_service.bank_service.model;

public enum TransactionStatus {
    PENDING,    // Đang chờ xác nhận
    APPROVED,   // Admin duyệt
    REJECTED,   // Admin từ chối
    EXPIRED,     // Hết hạn mã xác nhận
    FAILED,
    AWAITING_APPROVAL
}
