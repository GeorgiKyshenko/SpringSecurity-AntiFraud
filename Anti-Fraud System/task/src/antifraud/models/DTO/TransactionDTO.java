package antifraud.models.DTO;

import antifraud.constants.TransactionOutput;
import lombok.Value;

@Value
public class TransactionDTO {
    TransactionOutput result;
}
