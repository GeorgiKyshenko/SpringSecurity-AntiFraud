package antifraud.models.DTO;

import antifraud.constants.UserRole;
import lombok.*;

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
    @NotNull
    private UserRole role;

}
