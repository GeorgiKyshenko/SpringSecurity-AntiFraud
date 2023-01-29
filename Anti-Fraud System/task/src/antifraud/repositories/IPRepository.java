package antifraud.repositories;

import antifraud.models.database.IPs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPRepository extends JpaRepository<IPs, Long> {
    Optional<IPs> findIPsByIp(String ip);

    void deleteByIp(String ip);

    List<IPs> findAll();
}
