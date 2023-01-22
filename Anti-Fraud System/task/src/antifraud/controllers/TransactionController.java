package antifraud.controllers;

import antifraud.constants.TransactionOutput;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.Transaction;
import antifraud.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/api/antifraud/transaction")
    TransactionDTO transaction(@RequestBody @Valid Transaction req) throws CannotParseException, NegativeNumberException {
        log.info("Requested {}", req.getAmount());
        TransactionOutput result = transactionService.processing(req.getAmount());
        return new TransactionDTO(result);
    }
}
