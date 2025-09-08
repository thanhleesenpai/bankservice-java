package payment_service.payment_service.dto;

import java.math.BigDecimal;

public record PaymentMessage(
        String paymentId,
        String fromAccountId,
        String toAccountId,
        BigDecimal amount
) {}

