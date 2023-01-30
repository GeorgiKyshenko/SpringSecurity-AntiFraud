package antifraud.models.DTO;

import antifraud.constants.TransactionOutput;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeedback {
    Long transactionId;
    TransactionOutput feedback;
}
