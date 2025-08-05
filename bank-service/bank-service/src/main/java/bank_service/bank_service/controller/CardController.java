package bank_service.bank_service.controller;

import bank_service.bank_service.model.Card;
import bank_service.bank_service.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/my")
    public ResponseEntity<List<Card>> getCards() {
        String accountId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(cardService.getCardsByAccountId(accountId));
    }

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody Card card) {
        String accountId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        card.setAccountId(accountId); // Gán từ context
        return cardService.createCard(card)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Account does not exist"));
    }

//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<List<Card>> getCards(@PathVariable String accountId) {
//        return ResponseEntity.ok(cardService.getCardsByAccountId(accountId));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createCard(@RequestBody Card card) {
//        return cardService.createCard(card)
//                .<ResponseEntity<?>>map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.badRequest().body("Account does not exist"));
//    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        if (cardService.deleteCard(cardId)) {
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
