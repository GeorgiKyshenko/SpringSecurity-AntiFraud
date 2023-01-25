package antifraud.controllers;

import antifraud.database.User;
import antifraud.errors.*;
import antifraud.models.DTO.*;
import antifraud.models.UserRegisterRequest;
import antifraud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserRegisterResponse> userRegister(@RequestBody @Valid UserRegisterRequest user) throws ExistingUserException {
        UserRegisterResponse registerResponse = userService.register(user);
        return ResponseEntity.status(201).body(registerResponse);
    }

    @GetMapping("/api/auth/list")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('SUPPORT')")
    public List<UserRegisterResponse> findAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/api/auth/user/{username}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable String username) throws UserNotFoundException {
        userService.deleteUser(username);
        return ResponseEntity.ok(new DeleteUserResponse(username, "Deleted successfully!"));
    }

    @PutMapping("/api/auth/role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserRegisterResponse> updateRole(@RequestBody UserRoleUpdateRequest user) throws UserNotFoundException, NotViableRoleException, IllegalRoleUpdateException {
        UserRegisterResponse response = userService.updateUserRole(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/auth/access")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<AccessResponse> grantAccess(@RequestBody AccessRequest request) throws UserNotFoundException, IllegalActionException {
        AccessResponse response = userService.updateAccess(request);
        return ResponseEntity.status(200).body(response);
    }


}
