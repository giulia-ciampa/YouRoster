package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.AdminApprovalRequestDTO;
import giuliaciampa.YouRoster.dto.responses.AccountSummaryDTO;
import giuliaciampa.YouRoster.dto.responses.AdminApprovalResponseDTO;
import giuliaciampa.YouRoster.services.AccountService;
import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService, AuthService authService) {
        this.accountService = accountService;

    }

    //1. TROVA GLI ACCOUNT IN ATTESA DI ESSERE ACCETTATI
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/pending")
    public Page<AccountSummaryDTO> getPendingAccounts(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                      @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return accountService.getPendingAccounts(page, size, sortBy, direction);
    }

    //2.APPROVA E ASSEGNA RUOLO + SEDE (ADMIN)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/accept")
    public AdminApprovalResponseDTO approveAndassignRoles(@PathVariable UUID id, @RequestBody(required = false) AdminApprovalRequestDTO payload) {
        return accountService.approveAssignRolesAndOffice(id, payload);
    }


    //3. RIFIUTA E ELIMINA RICHIESTA (ADMIN)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/reject")
    public AdminApprovalResponseDTO rejectAccount(@PathVariable UUID id) {
        return accountService.rejectAccount(id);
    }


    // 4. DISABILITA ACCOUNT (ADMIN)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{accountId}/disable")
    public AdminApprovalResponseDTO disableAccount(@PathVariable UUID accountId) {
        return accountService.disableAccount(accountId);
    }

    //5. DATO UN RUOLO, TROVA GLI ACCOUNT CON QUEL RUOLO
    @GetMapping("/role")
    public Page<AccountSummaryDTO> getAccountsByRole(
            @RequestParam String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "user.surname") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return accountService.getAccountByRole(roleName, pageable);
    }

    //6. TROVA GLI ACCOUNT DISABILITATI
    @GetMapping("/disabled")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'AP E PAYROLL SPECIALIST')")
    public Page<AccountSummaryDTO> getSuspendedAccount(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "15") int size,
                                                       @RequestParam(defaultValue = "user.surname") String sortBy) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return accountService.getSuspendedAccount(pageable);
    }

    //7. RIATTIVA ACCOUNT DISABILITATO
    @PatchMapping("/{accountId}/reactivate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdminApprovalResponseDTO reactivateSuspendedAccount(
            @PathVariable UUID accountId,
            @RequestParam(required = false, defaultValue = "STAFF") String role
    ) {
        return accountService.reactivateSuspendedAccount(accountId, role);
    }

    //8. VISUALIZZA TUTTI GLI ACCOUNT ATTIVI
    @GetMapping("/active")
    public Page<AccountSummaryDTO> getActiveAccounts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "15") int size,
                                                     @RequestParam(defaultValue = "user.surname") String sortBy,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String surname) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        if ((name != null && !name.isBlank()) || (surname != null && !surname.isBlank())) {
            return accountService.searchActiveUsersByNameAndSurname(name, surname, pageable);
        }

        return accountService.getActiveAccounts(pageable);
    }


}
