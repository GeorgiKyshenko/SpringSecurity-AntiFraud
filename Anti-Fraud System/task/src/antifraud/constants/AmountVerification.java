package antifraud.constants;

import lombok.Getter;

@Getter
public enum AmountVerification {
    ALLOWED(200),
    MANUAL_PROCESSING(1500);

    private final long amount;

    AmountVerification(long amount) {
        this.amount = amount;
    }
}
