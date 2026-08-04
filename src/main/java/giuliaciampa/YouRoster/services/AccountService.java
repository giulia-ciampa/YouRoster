package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.AdminApprovalRequestDTO;
import giuliaciampa.YouRoster.dto.responses.AdminApprovalResponseDTO;
import giuliaciampa.YouRoster.emailTemplates.EmailTemplateBuilder;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.Office;
import giuliaciampa.YouRoster.entities.Role;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.AlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.BadRequestException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder bcrypt;
    private final RoleService roleService;
    private final OfficeService officeService;
    private final UserService userService;
    private final EmailService emailService;

    @Value("${login.url}")
    private String loginUrl;


    public AccountService(AccountRepository accountRepository, PasswordEncoder bcrypt, RoleService roleService, OfficeService officeService, UserService userService, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.bcrypt = bcrypt;
        this.roleService = roleService;
        this.officeService = officeService;
        this.userService = userService;

        this.emailService = emailService;
    }

    // CREA ACCOUNT PER L'ADMIN SE NON ESISTE
    public void saveAdmin(String defaultEmail, String defaultPassword) {
        boolean adminExist = accountRepository.existsByRoles_Name("ADMIN");

        if (!adminExist) {
            System.out.println("Nessun Admin trovato. Creazione Admin di default in corso...");

            Role adminRole = roleService.findRoleByName("ADMIN");

            saveAccount(defaultEmail, defaultPassword, Set.of(adminRole), true);

            System.out.println("Admin creato con successo!");
        } else {
            System.out.println("Account ADMIN già presente nel sistema.");
        }
    }

    //APPROVA E ASSEGNA RUOLO + SEDE (ADMIN)
    @Transactional
    public AdminApprovalResponseDTO approveAssignRolesAndOffice(UUID id, AdminApprovalRequestDTO payload) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("l'utente con id " + id + " non è stato trovato"));

        //1. map dei ruoli
        Set<Role> rolesToAssign = payload.roles().stream()
                .map(roleService::findRoleByName)
                .collect(Collectors.toSet());

        //2. recupera lo user legato all'account

        User user = userService.findByAccountId(account.getId());

        //3. verifica che tra i ruoli c'è coordinator
        boolean isCoordinator = rolesToAssign.stream().anyMatch(role -> role.getName().equalsIgnoreCase("COORDINATOR"));

        //4. se c'è coordinator assegna sede obbligatoriamente, altimenti assegno sede opzionale
        Office officeToAssign = null;


        if (isCoordinator) {
            if (payload.officeId() == null) {
                throw new BadRequestException("Il coordinatore " + user.getName() + " " + user.getSurname() + " deve avere una sede di riferimento");
            }
            officeToAssign = officeService.findById(payload.officeId());
        } else if (payload.officeId() != null) {
            officeToAssign = officeService.findById(payload.officeId());
            //recupero ufficio anche se l'account non è di un coordinatore ma gli viene assegnato comunque
        }

        //5. attivazione e aggiornamento account
        account.activate();
        account.setRoles(rolesToAssign);
        Account updatedAccount = accountRepository.save(account);


        //6. Assegnazione Sede allo User e salvataggio
        user.setReferenceOffice(officeToAssign);
        userService.saveUser(user);

        String roleNames = rolesToAssign.stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));

        //7. invio email di benvenuto
        String htmlBody = EmailTemplateBuilder.buildAccountApprovalEmail(
                user.getName(),
                roleNames,
                officeToAssign != null ? officeToAssign.getName() : "Non è stata assegnata nessuna sede specifica",
                loginUrl
        );

        emailService.sendHtmlEmail(
                account.getEmail(),
                "Account Approvato - Benvenuto in YouRoster!",
                htmlBody
        );


        if (isCoordinator) {
            return new AdminApprovalResponseDTO("L'account dell'utente " + user.getName() + " " + user.getSurname() + " è stato attivato con successo con ruolo " + roleNames + " nella sede " + officeToAssign.getName(), LocalDateTime.now());
        }

        return new AdminApprovalResponseDTO("L'account dell'utente " + user.getName() + " " + user.getSurname() + " è stato attivato con successo con ruolo " + roleNames, LocalDateTime.now());
    }


    //FIND BY ID
    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("L'account con id " + id + " non è stato trovato"));
    }

    // RIFIUTA E RIMOZIONE RICHIESTA (ADMIN)
    public void rejectAccount(UUID id) {
        Account accountFound = findById(id);
        accountRepository.delete(accountFound);
    }

    //METODO TROVA ACCOUNT PER EMAIL(per il login)
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'account con email " + email + " non è stato trovato"));
    }


    //METODO CONTROLLA SE L'EMAIL ESISTE GIA' A DB, SE ESISTE LANCIA ECCEZIONE (per la registrazione)
    public String checkIfEmailAlreadyExists(String email) {

        if (accountRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("L'utente con email " + email + " è già esistente");
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


    //TROVA GLI ACCOUNT IN ATTESA DI ESSERE ACCETTATI
    public Page<Account> getPendingAccounts(int page, int size, String sortBy) {


        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return accountRepository.findByIsActiveFalse(pageable);
    }

    //SALVA ACCOUNT PER AGGIORNAMENTO CREDENZIALI
    public Account save(Account account) {
        return accountRepository.save(account);
    }


}


