package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRoles_Name(String roleName);

    Page<Account> findByIsActiveFalse(Pageable pageable);

    Page<Account> findByRolesNameIgnoreCase(String roleName, Pageable pageable);

    Page<Account> findByIsActiveFalseAndRolesIsEmpty(Pageable pageable);
}
