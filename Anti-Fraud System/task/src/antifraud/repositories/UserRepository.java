package antifraud.repositories;

import antifraud.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    List<User> findAll();
    void deleteByUsernameIgnoreCase(String username);
}
