package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.AdminApprovalRequestDTO;
import giuliaciampa.YouRoster.dto.responses.AdminApprovalResponseDTO;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.services.AccountService;
import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    public AccountController(AccountService accountService, AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    //1. TROVA GLI ACCOUNT IN ATTESA DI ESSERE ACCETTATI
    @GetMapping("/pending")
    public Page<Account> getPendingAccounts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return accountService.getPendingAccounts(page, size, sortBy);
    }

    //2.APPROVA E ASSEGNA I RUOLI
    @PatchMapping("/{id}/accept")
    public AdminApprovalResponseDTO approveAndassignRoles(@PathVariable UUID id, @RequestBody @Validated AdminApprovalRequestDTO payload) {
        return authService.approveAndassignRoles(id, payload);
    }


    //3. RIFIUTA ED ELIMINA LA RICHIESTA
    @DeleteMapping("/{id}/reject")
    public void rejectAccount(@PathVariable UUID id) {
        authService.rejectAccount(id);
    }

}
