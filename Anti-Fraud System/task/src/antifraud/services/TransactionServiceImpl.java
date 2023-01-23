package antifraud.services;

import antifraud.constants.AmountVerification;
import antifraud.constants.TransactionOutput;
import antifraud.errors.CannotParseException;
import antifraud.errors.NegativeNumberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionOutput processing(String amount) throws CannotParseException, NegativeNumberException {

        if ((amount == null) || amount.startsWith("-") || amount.equals("0")) {
            throw new NegativeNumberException("Amount should be positive number");
        }

        long amountNumber;
        try {
            amountNumber = Long.parseLong(amount);
        } catch (Exception exception) {
            log.info("Catch section");
            throw new CannotParseException("Amount should contains only numbers");
        }

        if (amountNumber <= AmountVerification.ALLOWED.getAmount()) {
            return TransactionOutput.ALLOWED;
        } else if (amountNumber <= AmountVerification.MANUAL_PROCESSING.getAmount()) {
            return TransactionOutput.MANUAL_PROCESSING;
        } else {
            return TransactionOutput.PROHIBITED;
        }
    }
}
