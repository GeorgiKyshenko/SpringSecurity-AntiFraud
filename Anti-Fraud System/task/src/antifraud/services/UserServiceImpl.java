package antifraud.services;

import antifraud.constants.UserRole;
import antifraud.database.User;
import antifraud.errors.*;
import antifraud.models.DTO.AccessRequest;
import antifraud.models.DTO.AccessResponse;
import antifraud.models.DTO.UserRegisterResponse;
import antifraud.models.DTO.UserRoleUpdateRequest;
import antifraud.models.UserRegisterRequest;
import antifraud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserRegisterResponse register(UserRegisterRequest user) throws ExistingUserException {

        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent()) {
            throw new ExistingUserException("User already exists");
        }

        User userToSave = userRepository.count() == 0 ?
                User.builder()
                        .name(user.getName())
                        .username(user.getUsername())
                        .password(encoder.encode(user.getPassword()))
                        .role(UserRole.ADMINISTRATOR)
                        .isAccountNonLocked(true)
                        .build() :
                User.builder()
                        .name(user.getName())
                        .username(user.getUsername())
                        .password(encoder.encode(user.getPassword()))
                        .role(UserRole.MERCHANT)
                        .isAccountNonLocked(false)
                        .build();


        userRepository.save(userToSave);
        Optional<User> byUsername = userRepository.findByUsername(userToSave.getUsername());
        Long id = byUsername.get().getId();

        return new UserRegisterResponse(id, userToSave.getName(), userToSave.getUsername(), userToSave.getRole());
    }

    @Override
    public List<UserRegisterResponse> getAllUsers() {
        return userRepository.findAll(Sort.sort(User.class).by(User::getId).ascending())
                .stream()
                .map(user -> mapper.map(user, UserRegisterResponse.class)).toList();
    }

    @Override
    @Transactional
    public void deleteUser(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional
    public UserRegisterResponse updateUserRole(UserRoleUpdateRequest user) throws UserNotFoundException, NotViableRoleException, IllegalRoleUpdateException {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User not found");
        User existingUser = optionalUser.get();

        roleCheck(existingUser, user.getRole());
        existingUser.setRole(UserRole.valueOf(user.getRole()));
        UserRegisterResponse userToReturn = mapper.map(existingUser, UserRegisterResponse.class);
        userRepository.save(existingUser);
        return userToReturn;
    }

    @Override
    @Transactional
    public AccessResponse updateAccess(AccessRequest request) throws UserNotFoundException, IllegalActionException {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User not found");

        User existingUser = optionalUser.get();
        String access = existingUser.isAccountNonLocked() ? "unlocked" : "locked";
        if (existingUser.getRole().equals(UserRole.ADMINISTRATOR) || request.getOperation().equals("ADMINISTRATOR")) {
            throw new IllegalActionException("ADMINISTRATOR is already assign");
        } else if ((existingUser.isAccountNonLocked()) && request.getOperation().equals("LOCK")) {
            existingUser.setAccountNonLocked(false);
            access = "locked";
        } else if (!(existingUser.isAccountNonLocked()) && request.getOperation().equals("UNLOCK")) {
            existingUser.setAccountNonLocked(true);
            access = "unlocked";
        }
        userRepository.save(existingUser);
        return new AccessResponse(String.format("User %s %s!", existingUser.getUsername(), access));
    }

    private void roleCheck(User user, String role) throws NotViableRoleException, IllegalRoleUpdateException {
        if (!role.equals("SUPPORT") && (!role.equals("MERCHANT"))) {
            throw new NotViableRoleException(String.format("Role %s is not viable", role));
        } else if (user.getRole().equals(UserRole.valueOf(role))) {
            throw new IllegalRoleUpdateException("The user is already assign to this role");
        }
    }
}
