package antifraud.services;

import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.DTO.TransactionFeedback;
import antifraud.models.DTO.TransactionInfo;
import antifraud.models.database.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDTO processing(Transaction transaction) throws CannotParseException, NegativeNumberException;

    TransactionInfo feedbackInfo(TransactionFeedback feedback);

    List<TransactionInfo> getTransactions(String number);

    List<TransactionInfo> getTransactionsHistory();
}
