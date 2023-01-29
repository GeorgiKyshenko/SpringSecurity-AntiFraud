package antifraud.services;

import antifraud.errors.*;
import antifraud.models.DTO.AccessRequest;
import antifraud.models.DTO.AccessResponse;
import antifraud.models.DTO.UserRegisterResponse;
import antifraud.models.DTO.UserRoleUpdateRequest;
import antifraud.models.DTO.UserRegisterRequest;

import java.util.List;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest user) throws ExistingUserException;
    List<UserRegisterResponse> getAllUsers();
    void deleteUser(String username) throws UserNotFoundException;

    UserRegisterResponse updateUserRole(UserRoleUpdateRequest user) throws UserNotFoundException, NotViableRoleException, IllegalRoleUpdateException;

    AccessResponse updateAccess(AccessRequest request) throws UserNotFoundException, IllegalActionException;
}
