package bank_service.bank_service.controller;

import bank_service.bank_service.model.Balance;
import bank_service.bank_service.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private BalanceController balanceController;

    @BeforeEach
    void setupSecurityContext() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getBalance_BalanceExists_ReturnsOk() {
        // Arrange
        String accountId = "user123";
        Balance mockBalance = Balance.builder()
                .accountId(accountId)
                .availableBalance(BigDecimal.valueOf(1000))
                .holdBalance(BigDecimal.valueOf(200))
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(balanceService.getBalance(accountId)).thenReturn(Optional.of(mockBalance));

        // Act
        ResponseEntity<Balance> response = balanceController.getBalance();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockBalance, response.getBody());
        verify(balanceService, times(1)).getBalance(accountId);
    }

    @Test
    void getBalance_BalanceNotFound_ReturnsNotFound() {
        // Arrange
        String accountId = "user123";
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(balanceService.getBalance(accountId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Balance> response = balanceController.getBalance();

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(balanceService, times(1)).getBalance(accountId);
    }
}
