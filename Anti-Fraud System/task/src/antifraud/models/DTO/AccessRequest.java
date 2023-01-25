package antifraud.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class AccessRequest {
    @NotNull
    private String username;
    @NotNull
    private String operation;
}
