package antifraud.models.DTO;

import lombok.Value;

@Value
public class DeleteUserResponse {
    String username;
    String status;
}
