package antifraud.controllers;

import antifraud.constants.TransactionOutput;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.Transaction;
import antifraud.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/transaction")
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    TransactionDTO transaction(@RequestBody @Valid Transaction req) throws CannotParseException, NegativeNumberException {
        log.info("Requested {}", req.getAmount());
        TransactionOutput result = transactionService.processing(req.getAmount());
        return new TransactionDTO(result);
    }

    @GetMapping("/test")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Test Text");
    }
}
