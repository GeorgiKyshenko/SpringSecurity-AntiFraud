package antifraud.errors;

public class ExistingUserException extends BusinessException {

    public ExistingUserException() {
    }

    public ExistingUserException(String message) {
        super(message);
    }
}
