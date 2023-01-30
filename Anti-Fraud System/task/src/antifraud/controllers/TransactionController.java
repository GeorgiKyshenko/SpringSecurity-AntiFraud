package antifraud.controllers;

import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.DTO.TransactionFeedback;
import antifraud.models.DTO.TransactionInfo;
import antifraud.models.database.Transaction;
import antifraud.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/api/antifraud/transaction")
    @PreAuthorize("hasRole('MERCHANT')")
    public TransactionDTO transaction(@RequestBody @Valid Transaction req) throws CannotParseException, NegativeNumberException {
        log.info("Requested {}", req.getAmount());
        return transactionService.processing(req);
    }

    @PutMapping("/api/antifraud/transaction")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<TransactionInfo> transactionFeedback(@RequestBody TransactionFeedback feedback) {
        TransactionInfo transaction = transactionService.feedbackInfo(feedback);
        return ResponseEntity.status(200).body(transaction);
    }

    @GetMapping("/api/antifraud/history/{number}")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<List<TransactionInfo>> transactionsByCardNumber(@PathVariable String number) {
        List<TransactionInfo> transactionListByCardNumber = transactionService.getTransactions(number);
        return ResponseEntity.status(200).body(transactionListByCardNumber);
    }

    @GetMapping("/api/antifraud/history")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<List<TransactionInfo>> transactionsHistory() {
        List<TransactionInfo> transactionListHistory = transactionService.getTransactionsHistory();
        return ResponseEntity.status(200).body(transactionListHistory);
    }
}
