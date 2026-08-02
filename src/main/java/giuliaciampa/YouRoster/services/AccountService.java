package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //METODO TROVA ACCOUNT PER EMAIL

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'account con email " + email + " non è stato trovato"));
    }


}


