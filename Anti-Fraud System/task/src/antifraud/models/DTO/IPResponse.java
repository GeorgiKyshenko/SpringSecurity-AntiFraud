package antifraud.models.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IPResponse {
    private Long id;
    private String ip;
}

