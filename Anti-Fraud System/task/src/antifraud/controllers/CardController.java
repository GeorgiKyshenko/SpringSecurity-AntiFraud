package antifraud.controllers;

import antifraud.models.database.Card;
import antifraud.models.DTO.CardResponse;
import antifraud.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('SUPPORT')")
@RequestMapping("/api/antifraud/stolencard")
public class CardController {

    private final CardService cardService;
    private static final String SUCCESSFUL_REMOVE = "Card %s successfully removed!";

    @PostMapping
    public ResponseEntity<CardResponse> saveStolenCardNumber(@RequestBody @Valid Card stolenCard) {
        CardResponse card = cardService.saveCard(stolenCard);
        return ResponseEntity.status(200).body(card);
    }

    record DeleteCardResponse(String status) {
    }

    @DeleteMapping("{number}")
    public ResponseEntity<DeleteCardResponse> deleteCard(@PathVariable String number) {
        cardService.deleteCardFromDB(number);
        return ResponseEntity.status(200).body(new DeleteCardResponse(String.format(SUCCESSFUL_REMOVE, number)));
    }

    @GetMapping
    public List<CardResponse> getAllIPs() {
        return cardService.findAllCards();
    }
}
