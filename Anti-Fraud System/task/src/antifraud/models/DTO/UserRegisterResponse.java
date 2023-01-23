package antifraud.models.DTO;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UserRegisterResponse {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String username;

}
