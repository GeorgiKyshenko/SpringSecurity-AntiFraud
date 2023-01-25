package antifraud.models.DTO;

import antifraud.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class UserRoleUpdateRequest {

    @NotNull
    private String username;
    @NotNull
    private String role;
}
