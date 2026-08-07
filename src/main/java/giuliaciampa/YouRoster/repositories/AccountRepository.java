package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRoles_Name(String roleName);

    Page<Account> findByRolesNameIgnoreCase(String roleName, Pageable pageable);

    Page<Account> findByStatus(AccountStatus accountStatus, Pageable pageable);

    Page<Account> findByRolesNameIgnoreCaseAndStatus(String roleName, AccountStatus status, Pageable pageable);

    List<Account> findByRoles_NameIn(List<String> roleNames);

    Page<Account> findByUser_NameContainingIgnoreCaseAndUser_SurnameContainingIgnoreCaseAndStatus(
            String name,
            String surname,
            AccountStatus status,
            Pageable pageable
    );
}
