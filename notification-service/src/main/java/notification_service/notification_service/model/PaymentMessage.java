package notification_service.notification_service.model;

import java.math.BigDecimal;

public record PaymentMessage(
        String paymentId,
        String fromAccountId,
        String toAccountId,
        BigDecimal amount
) {}