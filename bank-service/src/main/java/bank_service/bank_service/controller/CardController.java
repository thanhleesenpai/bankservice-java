package bank_service.bank_service.controller;

import bank_service.bank_service.dto.CardFullInfoDTO;
import bank_service.bank_service.dto.CardWithUsernameDTO;
import bank_service.bank_service.model.Card;
import bank_service.bank_service.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/my")
    public ResponseEntity<List<Card>> getCards() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountId = userDetails.getUsername();
        return ResponseEntity.ok(cardService.getCardsByAccountId(accountId));
    }

    @GetMapping("/full-info")
    public List<CardFullInfoDTO> getAllCardsFullInfo() {
        return cardService.getAllCardsFullInfo();
    }
    @GetMapping("/{cardId}")
    public ResponseEntity<CardFullInfoDTO> getCardById(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getCardFullInfoById(cardId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<CardWithUsernameDTO>> getAllCardsWithUsername() {
        return ResponseEntity.ok(cardService.getAllCardsWithUsername());
    }

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody Card card) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountId = userDetails.getUsername();
        card.setAccountId(accountId); // Gán từ context
        return cardService.createCard(card)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Account does not exist"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{cardId}/status")
    public Card updateCardStatus(@PathVariable Long cardId, @RequestBody Card card) {
        return cardService.updateStatus(cardId, card.getStatus());
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        if (cardService.deleteCard(cardId)) {
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
