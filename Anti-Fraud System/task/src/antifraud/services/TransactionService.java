package antifraud.services;

import antifraud.constants.TransactionOutput;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;

public interface TransactionService {
    TransactionOutput processing(String amount) throws CannotParseException, NegativeNumberException;
}
