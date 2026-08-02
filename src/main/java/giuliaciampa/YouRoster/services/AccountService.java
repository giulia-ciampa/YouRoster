package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.Role;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.exceptions.UserAlreadyExistsException;
import giuliaciampa.YouRoster.repositories.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    //SALVA ACCOUNT
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

    //CONTROLLA SE ESISTE UN ACCOUNT CON IL RUOLO ADMIN
    public boolean existsAccountWithRole(String roleName) {
        return accountRepository.existsByRoles_Name(roleName);
    }


}


