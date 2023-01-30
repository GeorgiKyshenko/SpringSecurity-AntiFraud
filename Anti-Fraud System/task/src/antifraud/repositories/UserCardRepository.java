package antifraud.repositories;

import antifraud.models.database.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    Optional<UserCard> findLastByNumber(String number);
}
