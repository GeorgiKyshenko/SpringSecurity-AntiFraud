package antifraud.models.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class CardResponse {
    Long id;
    String number;
}
