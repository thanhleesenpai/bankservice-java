package bank_service.bank_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {

    @Id
    private String accountId;

    @Column(nullable = false)
    private BigDecimal availableBalance;

    @Column(nullable = false)
    private BigDecimal holdBalance;
}
