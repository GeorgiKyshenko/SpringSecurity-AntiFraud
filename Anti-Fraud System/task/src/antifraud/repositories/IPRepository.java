package antifraud.repositories;

import antifraud.database.IPs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPRepository extends JpaRepository<IPs, Long> {
    Optional<IPs> findIPsByIp(String ip);
    void deleteByIp(String ip);
}
