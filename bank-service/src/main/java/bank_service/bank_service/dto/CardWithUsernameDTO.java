package bank_service.bank_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CardWithUsernameDTO {
    private Long cardId;
    private String accountId;
    private String customerName;
    private String cardType;
    private LocalDate expiryDate;
    private String status;
}
