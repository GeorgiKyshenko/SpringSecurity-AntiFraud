package antifraud.services;

import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.database.Transaction;

public interface TransactionService {
    TransactionDTO processing(Transaction transaction) throws CannotParseException, NegativeNumberException;
}
