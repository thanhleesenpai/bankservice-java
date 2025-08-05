package bank_service.bank_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String password;
}
