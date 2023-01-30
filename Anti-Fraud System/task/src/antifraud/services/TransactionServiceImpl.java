package antifraud.services;

import antifraud.constants.AmountVerification;
import antifraud.constants.TransactionOutput;
import antifraud.models.DTO.TransactionFeedback;
import antifraud.models.DTO.TransactionInfo;
import antifraud.models.database.Card;
import antifraud.models.database.IPs;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import antifraud.models.DTO.TransactionDTO;
import antifraud.models.database.Transaction;
import antifraud.models.database.UserCard;
import antifraud.repositories.CardRepository;
import antifraud.repositories.IPRepository;
import antifraud.repositories.TransactionRepository;
import antifraud.repositories.UserCardRepository;
import antifraud.utils.CardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final IPRepository ipRepository;
    private final CardRepository cardRepository;
    private final UserCardRepository userCardRepository;
    private final ModelMapper mapper;
    private static final int NUMBER_OF_REQUESTS = 3;
    private UserCard checkingCard;

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
        Transaction newTransaction = transactionRepository.save(transaction);

        Optional<IPs> ip = ipRepository.findIPsByIp(transaction.getIp());
        Optional<Card> card = cardRepository.findByNumber(transaction.getNumber());
        List<Transaction> listOfTransactions = transactionRepository
                .findByNumberAndDateBetween(transaction.getNumber(), transaction.getDate().minusHours(1), transaction.getDate());
        List<String> stringResults = new ArrayList<>();
        long iPRequests = listOfTransactions.stream().map(Transaction::getIp).distinct().count();
        long regionRequests = listOfTransactions.stream().map(Transaction::getRegion).distinct().count();

        if (newTransaction.getNumber() != null && card.isEmpty()) {
            if (userCardRepository.findLastByNumber(newTransaction.getNumber()).isEmpty()) {
                checkingCard = userCardRepository.save(new UserCard(newTransaction.getNumber()));
            } else {
                checkingCard = userCardRepository.findLastByNumber(newTransaction.getNumber()).get();
            }
        }

        if (checkForProhibitedActions(iPRequests, regionRequests, stringResults, amountNumber, ip, card, checkingCard)) {
            newTransaction.setResult(TransactionOutput.PROHIBITED);
            transactionRepository.save(newTransaction);
            return new TransactionDTO(TransactionOutput.PROHIBITED, stringResults.stream().sorted().collect(Collectors.joining(", ")));
        } else if (checksForManualProcessing(iPRequests, regionRequests, stringResults, amountNumber, checkingCard)) {
            newTransaction.setResult(TransactionOutput.MANUAL_PROCESSING);
            transactionRepository.save(newTransaction);
            return new TransactionDTO(TransactionOutput.MANUAL_PROCESSING, stringResults.stream().sorted().collect(Collectors.joining(", ")));
        } else {
            newTransaction.setResult(TransactionOutput.ALLOWED);
            transactionRepository.save(newTransaction);
            return new TransactionDTO(TransactionOutput.ALLOWED, "none");
        }
    }

    @Override
    @Transactional
    public TransactionInfo feedbackInfo(TransactionFeedback feedback) {
        Optional<Transaction> transactionById = transactionRepository.findById(feedback.getTransactionId());

        checkForValidFeedback(feedback, transactionById);
        Transaction transaction = transactionById.get();
        setMaxAllowedValueAndMaxManualValu(feedback, transaction);
        userCardRepository.save(checkingCard);
        transaction.setFeedback(feedback.getFeedback());
        transactionRepository.save(transaction);
        return mapper.map(transaction, TransactionInfo.class);

    }
    private void setMaxAllowedValueAndMaxManualValu(TransactionFeedback feedback, Transaction transaction) {
        if (transaction.getResult().equals(TransactionOutput.ALLOWED) && feedback.getFeedback().equals(TransactionOutput.MANUAL_PROCESSING)) {
            checkingCard.setAllowedValue((int) Math.ceil(0.8 * checkingCard.getAllowedValue() - (0.2 * Integer.parseInt(transaction.getAmount()))));
        } else if (transaction.getResult().equals(TransactionOutput.ALLOWED) && feedback.getFeedback().equals(TransactionOutput.PROHIBITED)) {
            checkingCard.setAllowedValue((int) Math.ceil(0.8 * checkingCard.getAllowedValue() - (0.2 * Integer.parseInt(transaction.getAmount()))));
            checkingCard.setManualValue((int) Math.ceil(0.8 * checkingCard.getManualValue() - (0.2 * Integer.parseInt(transaction.getAmount()))));
        } else if (transaction.getResult().equals(TransactionOutput.MANUAL_PROCESSING) && feedback.getFeedback().equals(TransactionOutput.ALLOWED)) {
            checkingCard.setAllowedValue((int) Math.ceil(0.8 * checkingCard.getAllowedValue() + (0.2 * Integer.parseInt(transaction.getAmount()))));
        } else if (transaction.getResult().equals(TransactionOutput.MANUAL_PROCESSING) && feedback.getFeedback().equals(TransactionOutput.PROHIBITED)) {
            checkingCard.setManualValue((int) Math.ceil(0.8 * checkingCard.getManualValue() - (0.2 * Integer.parseInt(transaction.getAmount()))));
        } else if (transaction.getResult().equals(TransactionOutput.PROHIBITED) && feedback.getFeedback().equals(TransactionOutput.ALLOWED)) {
            checkingCard.setAllowedValue((int) Math.ceil(0.8 * checkingCard.getAllowedValue() + (0.2 * Integer.parseInt(transaction.getAmount()))));
            checkingCard.setManualValue((int) Math.ceil(0.8 * checkingCard.getManualValue() + (0.2 * Integer.parseInt(transaction.getAmount()))));
        } else if (transaction.getResult().equals(TransactionOutput.PROHIBITED) && feedback.getFeedback().equals(TransactionOutput.MANUAL_PROCESSING)) {
            checkingCard.setManualValue((int) Math.ceil(0.8 * checkingCard.getManualValue() + (0.2 * Integer.parseInt(transaction.getAmount()))));
        }
    }

    private static void checkForValidFeedback(TransactionFeedback feedback, Optional<Transaction> transactionById) {
        if (transactionById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (!transactionById.get().getFeedback().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } else if (transactionById.get().getResult().equals(feedback.getFeedback())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public List<TransactionInfo> getTransactions(String number) {
        CardValidator.validateCardNumber(number);
        Optional<Transaction> optionalTransaction = transactionRepository.findFirstByNumber(number);
        if (optionalTransaction.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transactionRepository.findAllByNumber(number)
                .stream().map(t -> mapper.map(t, TransactionInfo.class)).toList();
    }

    @Override
    public List<TransactionInfo> getTransactionsHistory() {
        return transactionRepository.findAll()
                .stream().map(t -> mapper.map(t, TransactionInfo.class)).toList();
    }


    private boolean checksForManualProcessing
            (long iPRequests, long regionRequests, List<String> result, long amountNumber, UserCard checkingCard) {
        boolean flag = false;

        if (checkingCard != null) {
            if ((amountNumber > checkingCard.getAllowedValue() && amountNumber <= checkingCard.getManualValue())) {
                result.add("amount");
                flag = true;
            }
        } else if ((amountNumber > AmountVerification.ALLOWED.getAmount() && amountNumber <= AmountVerification.MANUAL_PROCESSING.getAmount())) {
            result.add("amount");
            flag = true;
        }


        if (iPRequests == NUMBER_OF_REQUESTS && regionRequests == NUMBER_OF_REQUESTS) {
            result.add("ip-correlation");
            result.add("region-correlation");
            flag = true;
        } else if (iPRequests == NUMBER_OF_REQUESTS) {
            result.add("ip-correlation");
            flag = true;
        } else if (regionRequests == NUMBER_OF_REQUESTS) {
            result.add("region-correlation");
            flag = true;
        }
        return flag;
    }

    private boolean checkForProhibitedActions(long iPRequests, long regionRequests, List<String> result,
                                              long amountNumber, Optional<IPs> ip, Optional<Card> card, UserCard checkingCard) {
        boolean flag = false;

        if (checkingCard != null) {
            if (amountNumber > checkingCard.getManualValue()) {
                result.add("amount");
                flag = true;
            }
        } else if (amountNumber > AmountVerification.MANUAL_PROCESSING.getAmount()) {
            result.add("amount");
            flag = true;
        }

        if (iPRequests > NUMBER_OF_REQUESTS && regionRequests > NUMBER_OF_REQUESTS) {
            result.add("ip-correlation");
            result.add("region-correlation");
            flag = true;
        } else if (iPRequests > NUMBER_OF_REQUESTS) {
            result.add("ip-correlation");
            flag = true;
        } else if (regionRequests > NUMBER_OF_REQUESTS) {
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
