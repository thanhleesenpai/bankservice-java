package bank_service.bank_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String cardType;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String status;
}
