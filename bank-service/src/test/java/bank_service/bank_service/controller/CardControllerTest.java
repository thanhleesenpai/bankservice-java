package bank_service.bank_service.controller;

import bank_service.bank_service.model.Card;
import bank_service.bank_service.service.CardService;
import bank_service.bank_service.dto.CardFullInfoDTO;
import bank_service.bank_service.dto.CardWithUsernameDTO;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setupSecurityContext() {
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    // ====== TEST GET /api/cards/my ======
    @Test
    void getCards_CardsExist_ReturnsOk() {
        // Arrange
        String accountId = "user123";
        List<Card> mockCards = List.of(
                Card.builder().cardId(1L).accountId(accountId).cardType("CREDIT").expiryDate(LocalDate.now().plusYears(5)).status("active").build()
        );

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(cardService.getCardsByAccountId(accountId)).thenReturn(mockCards);

        // Act
        ResponseEntity<List<Card>> response = cardController.getCards();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCards, response.getBody());
        verify(cardService, times(1)).getCardsByAccountId(accountId);
    }

    @Test
    void getCards_NoCards_ReturnsEmptyList() {
        // Arrange
        String accountId = "user123";
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(cardService.getCardsByAccountId(accountId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Card>> response = cardController.getCards();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(cardService, times(1)).getCardsByAccountId(accountId);
    }

    // ====== TEST POST /api/cards ======
    @Test
    void createCard_AccountExists_ReturnsOk() {
        // Arrange
        String accountId = "user123";
        Card newCard = Card.builder().cardType("DEBIT").build();
        Card savedCard = Card.builder().cardId(1L).accountId(accountId).cardType("DEBIT").expiryDate(LocalDate.now().plusYears(5)).status("active").build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(cardService.createCard(any(Card.class))).thenReturn(Optional.of(savedCard));

        // Act
        ResponseEntity<?> response = cardController.createCard(newCard);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedCard, response.getBody());
        verify(cardService, times(1)).createCard(any(Card.class));
    }

    @Test
    void createCard_AccountNotExist_ReturnsBadRequest() {
        // Arrange
        String accountId = "user123";
        Card newCard = Card.builder().cardType("DEBIT").build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(accountId);
        when(cardService.createCard(any(Card.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = cardController.createCard(newCard);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Account does not exist", response.getBody());
        verify(cardService, times(1)).createCard(any(Card.class));
    }

    // ====== TEST GET /api/cards/full-info ======
    @Test
    void getAllCardsFullInfo_ReturnsList() {
        // Arrange
        List<CardFullInfoDTO> mockList = List.of(
                new CardFullInfoDTO(1L, "user123", "John", "john@example.com", "123456789",
                        "CREDIT", LocalDate.now().plusYears(5), "active",
                        BigDecimal.valueOf(1000), BigDecimal.valueOf(200))
        );

        when(cardService.getAllCardsFullInfo()).thenReturn(mockList);

        // Act
        List<CardFullInfoDTO> response = cardController.getAllCardsFullInfo();

        // Assert
        assertEquals(mockList, response);
        verify(cardService, times(1)).getAllCardsFullInfo();
    }

    // ====== TEST GET /api/cards/{cardId} ======
    @Test
    void getCardById_CardExists_ReturnsOk() {
        // Arrange
        Long cardId = 1L;
        CardFullInfoDTO mockCard = new CardFullInfoDTO(cardId, "user123", "John", "john@example.com", "123456789",
                "CREDIT", LocalDate.now().plusYears(5), "active",
                BigDecimal.valueOf(1000), BigDecimal.valueOf(200));

        when(cardService.getCardFullInfoById(cardId)).thenReturn(mockCard);

        // Act
        ResponseEntity<CardFullInfoDTO> response = cardController.getCardById(cardId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCard, response.getBody());
        verify(cardService, times(1)).getCardFullInfoById(cardId);
    }

    @Test
    void getCardById_CardNotFound_ThrowsException() {
        // Arrange
        Long cardId = 999L;
        when(cardService.getCardFullInfoById(cardId)).thenThrow(new RuntimeException("Card not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cardController.getCardById(cardId));
        verify(cardService, times(1)).getCardFullInfoById(cardId);
    }

    // ====== TEST GET /api/cards/all (ADMIN) ======
    @Test
    void getAllCardsWithUsername_ReturnsList() {
        // Arrange
        List<CardWithUsernameDTO> mockList = List.of(
                new CardWithUsernameDTO(1L, "user123", "John", "CREDIT", LocalDate.now().plusYears(5), "active")
        );

        when(cardService.getAllCardsWithUsername()).thenReturn(mockList);

        // Act
        ResponseEntity<List<CardWithUsernameDTO>> response = cardController.getAllCardsWithUsername();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockList, response.getBody());
        verify(cardService, times(1)).getAllCardsWithUsername();
    }

    // ====== TEST PATCH /api/cards/{cardId}/status ======
    @Test
    void updateCardStatus_CardExists_ReturnsUpdatedCard() {
        // Arrange
        Long cardId = 1L;
        Card cardRequest = Card.builder().status("inactive").build();
        Card updatedCard = Card.builder().cardId(cardId).accountId("user123").cardType("CREDIT")
                .expiryDate(LocalDate.now().plusYears(5)).status("inactive").build();

        when(cardService.updateStatus(cardId, "inactive")).thenReturn(updatedCard);

        // Act
        Card response = cardController.updateCardStatus(cardId, cardRequest);

        // Assert
        assertEquals(updatedCard, response);
        verify(cardService, times(1)).updateStatus(cardId, "inactive");
    }

    @Test
    void updateCardStatus_CardNotFound_ThrowsException() {
        // Arrange
        Long cardId = 999L;
        Card cardRequest = Card.builder().status("inactive").build();

        when(cardService.updateStatus(cardId, "inactive"))
                .thenThrow(new RuntimeException("Card not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cardController.updateCardStatus(cardId, cardRequest));
        verify(cardService, times(1)).updateStatus(cardId, "inactive");
    }

    // ====== TEST DELETE /api/cards/{cardId} ======
    @Test
    void deleteCard_CardExists_ReturnsOk() {
        // Arrange
        Long cardId = 1L;
        when(cardService.deleteCard(cardId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = cardController.deleteCard(cardId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted", response.getBody());
        verify(cardService, times(1)).deleteCard(cardId);
    }

    @Test
    void deleteCard_CardNotFound_ReturnsNotFound() {
        // Arrange
        Long cardId = 999L;
        when(cardService.deleteCard(cardId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = cardController.deleteCard(cardId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(cardService, times(1)).deleteCard(cardId);
    }

}
