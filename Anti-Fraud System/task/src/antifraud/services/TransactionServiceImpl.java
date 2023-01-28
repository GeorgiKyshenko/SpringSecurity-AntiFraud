package antifraud.services;

import antifraud.constants.AmountVerification;
import antifraud.constants.TransactionOutput;
import antifraud.database.Card;
import antifraud.database.IPs;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.Transaction;
import antifraud.repositories.CardRepository;
import antifraud.repositories.IPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final IPRepository ipRepository;
    private final CardRepository cardRepository;

    @Override
    public TransactionDTO processing(Transaction transaction) throws CannotParseException, NegativeNumberException {

        Optional<IPs> ip = ipRepository.findIPsByIp(transaction.getIp());
        Optional<Card> card = cardRepository.findCardByNumber(transaction.getNumber());

        if ((transaction.getAmount() == null) || transaction.getAmount().startsWith("-") || transaction.getAmount().equals("0")) {
            throw new NegativeNumberException("Amount should be positive number");
        }

        long amountNumber;
        try {
            amountNumber = Long.parseLong(transaction.getAmount());
        } catch (Exception exception) {
            log.info("Catch section");
            throw new CannotParseException("Amount should contains only numbers");
        }

        if (amountNumber <= AmountVerification.ALLOWED.getAmount() && ip.isEmpty() && card.isEmpty()) {
            return new TransactionDTO(TransactionOutput.ALLOWED, "none");
        } else if (amountNumber <= AmountVerification.MANUAL_PROCESSING.getAmount() && ip.isEmpty() && card.isEmpty()) {
            return new TransactionDTO(TransactionOutput.MANUAL_PROCESSING, "amount");
        } else {
            StringJoiner stringJoiner = new StringJoiner(", ");
            if (Long.parseLong(transaction.getAmount()) >= AmountVerification.MANUAL_PROCESSING.getAmount()) {
                stringJoiner.add("amount");
            }
            if (ip.isPresent() && card.isPresent()) {
                stringJoiner.add("card-number");
                stringJoiner.add("ip");
            } else if (card.isPresent()) {
                stringJoiner.add("card-number");
            } else if (ip.isPresent()) {
                stringJoiner.add("ip");
            }
            return new TransactionDTO(TransactionOutput.PROHIBITED, stringJoiner.toString());
        }
    }
}
