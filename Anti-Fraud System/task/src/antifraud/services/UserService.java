package antifraud.services;

import antifraud.errors.ExistingUserException;
import antifraud.errors.UserNotFoundException;
import antifraud.models.DTO.UserRegisterResponse;
import antifraud.models.UserRegisterRequest;

import java.util.List;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest user) throws ExistingUserException;
    List<UserRegisterResponse> getAllUsers();
    void deleteUser(String username) throws UserNotFoundException;
}
