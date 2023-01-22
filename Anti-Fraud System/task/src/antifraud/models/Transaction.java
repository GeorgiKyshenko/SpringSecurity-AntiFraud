package antifraud.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter@Setter
@NoArgsConstructor
public class Transaction {
//    @Positive(message = "Amount should be positive number") if Long amount;
    String amount;
}
