package giuliaciampa.YouRoster.repositories;


import giuliaciampa.YouRoster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByTaxCode(String taxCode);

    boolean existsByDocumentNumber(String documentNumber);

    Optional<User> findByAccount_Id(UUID accountId);
}
