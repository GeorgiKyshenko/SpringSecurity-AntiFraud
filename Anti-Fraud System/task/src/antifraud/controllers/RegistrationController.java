package antifraud.controllers;

import antifraud.database.User;
import antifraud.errors.ExistingUserException;
import antifraud.errors.UserNotFoundException;
import antifraud.models.DTO.DeleteUserResponse;
import antifraud.models.DTO.UserRegisterResponse;
import antifraud.models.UserRegisterRequest;
import antifraud.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("hasRole('USER')")
    public List<UserRegisterResponse> findAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/api/auth/user/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable String username) throws UserNotFoundException {
        userService.deleteUser(username);
        return ResponseEntity.ok(new DeleteUserResponse(username, "Deleted successfully!"));
    }

}
