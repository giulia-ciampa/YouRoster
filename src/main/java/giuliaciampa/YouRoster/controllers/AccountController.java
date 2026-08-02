package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //TROVA GLI ACCOUNT IN ATTESA DI ESSERE ACCETTATI
    @GetMapping("/pending")
    public Page<Account> getPendingAccounts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return accountService.getPendingAccounts(page, size, sortBy);
    }

}
