package antifraud.models.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class UserRegisterRequest {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
