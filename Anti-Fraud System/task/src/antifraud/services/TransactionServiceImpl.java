package antifraud.services;

import antifraud.constants.AmountVerification;
import antifraud.constants.TransactionOutput;
import antifraud.models.database.Card;
import antifraud.models.database.IPs;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.database.Transaction;
import antifraud.repositories.CardRepository;
import antifraud.repositories.IPRepository;
import antifraud.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final IPRepository ipRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public TransactionDTO processing(Transaction transaction) throws CannotParseException, NegativeNumberException {

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
        transactionRepository.save(transaction);
        Optional<IPs> ip = ipRepository.findIPsByIp(transaction.getIp());
        Optional<Card> card = cardRepository.findCardByNumber(transaction.getNumber());
        List<Transaction> listOfTransactions = transactionRepository
                .findByNumberAndDateBetween(transaction.getNumber(), transaction.getDate().minusHours(1), transaction.getDate());
        List<String> stringResults = new ArrayList<>();

        if (checkForCorrelationOnProhibited(listOfTransactions, stringResults, amountNumber, ip, card)) {
            return new TransactionDTO(TransactionOutput.PROHIBITED, stringResults.stream().sorted().collect(Collectors.joining(", ")));
        } else if (checkForCorrelationOnManualProcessing(listOfTransactions, stringResults, amountNumber)) {
            return new TransactionDTO(TransactionOutput.MANUAL_PROCESSING, stringResults.stream().sorted().collect(Collectors.joining(", ")));
        } else {
            return new TransactionDTO(TransactionOutput.ALLOWED, "none");
        }
    }


    private boolean checkForCorrelationOnManualProcessing
            (List<Transaction> listOfTransactions, List<String> result, long amountNumber) {
        long iPRequests = listOfTransactions.stream().map(Transaction::getIp).distinct().count();
        long regionRequests = listOfTransactions.stream().map(Transaction::getRegion).distinct().count();
        boolean flag = false;

        if (amountNumber > AmountVerification.ALLOWED.getAmount() && amountNumber <= AmountVerification.MANUAL_PROCESSING.getAmount()) {
            result.add("amount");
            flag = true;
        }
        if (iPRequests == 3 && regionRequests == 3) {
            result.add("ip-correlation");
            result.add("region-correlation");
            flag = true;
        } else if (iPRequests == 3) {
            result.add("ip-correlation");
            flag = true;
        } else if (regionRequests == 3) {
            result.add("region-correlation");
            flag = true;
        }
        return flag;
    }

    private boolean checkForCorrelationOnProhibited(List<Transaction> listOfTransactions, List<String> result,
                                                    long amountNumber, Optional<IPs> ip, Optional<Card> card) {
        long iPRequests = listOfTransactions.stream().map(Transaction::getIp).distinct().count();
        long regionRequests = listOfTransactions.stream().map(Transaction::getRegion).distinct().count();
        boolean flag = false;

        if (amountNumber > AmountVerification.MANUAL_PROCESSING.getAmount()) {
            result.add("amount");
            flag = true;
        }
        if (iPRequests > 3 && regionRequests > 3) {
            result.add("ip-correlation");
            result.add("region-correlation");
            flag = true;
        } else if (iPRequests > 3) {
            result.add("ip-correlation");
            flag = true;
        } else if (regionRequests > 3) {
            result.add("region-correlation");
            flag = true;
        }
        if (ip.isPresent() && card.isPresent()) {
            result.add("card-number");
            result.add("ip");
            flag = true;
        } else if (card.isPresent()) {
            result.add("card-number");
            flag = true;
        } else if (ip.isPresent()) {
            result.add("ip");
            flag = true;
        }
        return flag;
    }
}


//    private StringJoiner outputResult(Transaction transaction, Optional<IPs> ip, Optional<Card> card) {
//        StringJoiner stringJoiner = new StringJoiner(", ");
//        if (Long.parseLong(transaction.getAmount()) >= AmountVerification.MANUAL_PROCESSING.getAmount()) {
//            stringJoiner.add("amount");
//        }
//        if (ip.isPresent() && card.isPresent()) {
//            stringJoiner.add("card-number");
//            stringJoiner.add("ip");
//        } else if (card.isPresent()) {
//            stringJoiner.add("card-number");
//        } else if (ip.isPresent()) {
//            stringJoiner.add("ip");
//        }
//        return stringJoiner;
//    }
