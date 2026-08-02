package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.Role;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.exceptions.UserAlreadyExistsException;
import giuliaciampa.YouRoster.repositories.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder bcrypt;
    private final RoleService roleService;

    public AccountService(AccountRepository accountRepository, PasswordEncoder bcrypt, RoleService roleService) {
        this.accountRepository = accountRepository;
        this.bcrypt = bcrypt;
        this.roleService = roleService;
    }

    //FIND BY ID
    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("L'account con id " + id + " non è stato trovato"));
    }

    //METODO TROVA ACCOUNT PER EMAIL
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'account con email " + email + " non è stato trovato"));
    }

    //CONTROLLA SE L'EMAIL ESISTE GIA'
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    //METODO CONTROLLA SE L'EMAIL ESISTE GIA' A DB, SE ESISTE LANCIA ECCEZIONE
    public String checkIfEmailAlreadyExists(String email) {

        if (accountRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("L'utente con email " + email + " è già esistente");
        }
        return email;
    }

    //CREA E SALVA NUOVO ACCOUNT PER LA REGISTRAZIONE E ADMIN DI DEFAULT
    public Account saveAccount(String email, String password, Set<Role> roles, boolean isActive) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(bcrypt.encode(password));

        if (isActive) {
            account.activate();
        }

        if (roles != null && !roles.isEmpty()) {
            account.setRoles(roles);
        } else {
            Role defaultRole = roleService.findRoleByName("STAFF");
            account.setRoles(Set.of(defaultRole));
        }
        return accountRepository.save(account);
    }

    //AGGIORNA ACCOUNT ESISTENTE
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    // RIFIUTA E RIMUOOVE ACCOUNT
    public void deleteAccount(UUID id) {
        Account account = findById(id);
        accountRepository.delete(account);
    }

    //CONTROLLA SE ESISTE UN ACCOUNT CON IL RUOLO ADMIN
    public boolean existsAccountWithRole(String roleName) {
        return accountRepository.existsByRoles_Name(roleName);
    }


    //TROVA GLI ACCOUNT IN ATTESA DI ESSERE ACCETTATI
    public Page<Account> getPendingAccounts(int page, int size, String sortBy) {


        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return accountRepository.findByIsActiveFalse(pageable);
    }

}


