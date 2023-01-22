package antifraud.errors;

import antifraud.errors.BusinessException;

public class NegativeNumberException extends BusinessException {
    public NegativeNumberException(String message) {
        super(message);
    }
}

