package bank_service.bank_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String paymentId;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
}
