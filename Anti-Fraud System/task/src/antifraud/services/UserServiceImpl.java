package antifraud.services;

import antifraud.constants.UserRole;
import antifraud.database.User;
import antifraud.errors.ExistingUserException;
import antifraud.errors.UserNotFoundException;
import antifraud.models.DTO.UserRegisterResponse;
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

        User userToSave = User.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(userToSave);
        Optional<User> byUsername = userRepository.findByUsername(userToSave.getUsername());
        Long id = byUsername.get().getId();

        return new UserRegisterResponse(id, userToSave.getName(), userToSave.getUsername());
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
}
