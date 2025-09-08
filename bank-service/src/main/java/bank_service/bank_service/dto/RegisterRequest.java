package bank_service.bank_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String customerName;
}
