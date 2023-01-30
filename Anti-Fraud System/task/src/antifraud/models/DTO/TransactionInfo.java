package antifraud.models.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfo {
    private Long transactionId;
    private Long amount;
    @NotNull
    private String ip;
    @NotNull
    private String number;
    @NotNull
    private String region;
    private LocalDateTime date;
    private String result;
    private String feedback;
}
