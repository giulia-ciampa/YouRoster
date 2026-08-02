package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.UserRegistrationRequestDTO;
import giuliaciampa.YouRoster.dto.responses.UserRegistrationResponseDTO;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.Role;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthService {
    private final AccountService accountService;
    private final UserService userService;
    private final RoleService roleService;


    public AuthService(AccountService accountService, UserService userService, RoleService roleService) {
        this.accountService = accountService;
        this.userService = userService;
        this.roleService = roleService;
    }

    //METODO SALVA NUOVO ACCOUNT-REGISTER E CREA UNO USER

    @Transactional
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO payload) {

        // 1. Controlli sui dati già esistenti nel DB
        String correctEmail = accountService.checkIfEmailAlreadyExists(payload.email());
        String correctTaxCode = userService.checkIfTaxCodeAlreadyExists(payload.taxCode());
        String correctDocumentNumber = userService.checkIfDocumentNumberAlreadyExists(payload.documentNumber());

        //2. controllo uguaglianza password
        if (!payload.password().equals(payload.confirmPassword())) {
            throw new ValidationException("Le password inserite non coincidono, riprova.");
        }

        //3. Controllo formato documento in base alla nazionalità
        String cleanNumber = payload.documentNumber().trim().toUpperCase();
        boolean isValid;

        if ("IT".equalsIgnoreCase(payload.nationality().trim()) || "ITALIA".equalsIgnoreCase(payload.nationality().trim())) {
            isValid = switch (payload.documentType()) {
                case IDENTITY_CARD -> cleanNumber.matches("^[A-Z]{2}[0-9]{5}[A-Z]{2}$");
                case DRIVING_LICENSE -> cleanNumber.matches("^[A-Z]{2}[0-9]{7}[A-Z]{1}$");
                case PASSPORT -> cleanNumber.matches("^[A-Z0-9]{5,20}$");
            };
        } else {
            isValid = cleanNumber.matches("^[A-Z0-9]{5,20}$");
        }

        if (!isValid) {
            throw new ValidationException("Il formato del numero di documento non è valido per la nazionalità " + payload.nationality());
        }

        // 4. Salva Account
        Role staff = roleService.findRoleByName("STAFF");
        Account savedAccount = accountService.saveAccount(correctEmail, payload.password(), Set.of(staff), false);


        // 5. Crea e salva lo User associato
        User user = new User();
        user.setName(payload.name());
        user.setSurname(payload.surname());
        user.setNationality(payload.nationality().trim().toUpperCase());
        user.setTaxCode(correctTaxCode);
        user.setDateOfBirth(payload.dateOfBirth());
        user.setPlaceOfBirth(payload.placeOfBirth());
        user.setPhoneNumber(payload.phoneNumber());
        user.setAddress(payload.streetAddress());
        user.setHouseNumber(payload.houseNumber());
        user.setZipCode(payload.zipCode());
        user.setCity(payload.city());
        user.setProvince(payload.province());
        user.setPhotoUrl("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());
        user.setIban(payload.iban().trim().toUpperCase());
        user.setDocumentNumber(correctDocumentNumber);
        user.setDocumentType(payload.documentType());
        user.setIssueDate(payload.issueDate());
        user.setExpirationDate(payload.expirationDate());
        user.setAccount(savedAccount);


        User savedUser = userService.saveUser(user);
// 6. Return DTO
        return new UserRegistrationResponseDTO(
                savedUser.getName(),
                savedUser.getSurname(),
                savedUser.getAccount().getEmail(),
                "Registrazione avvenuta con successo, presto l'amministratore potrà visualizzare la tua richiesta di attivazione.",
                LocalDateTime.now()
        );
    }

    // CREA ACCOUNT PER L'ADMIN SE NON ESISTE
    public void saveAdmin(String defaultEmail, String defaultPassword) {
        boolean adminExist = accountService.existsAccountWithRole("ADMIN");

        if (!adminExist) {
            System.out.println("Nessun Admin trovato. Creazione Admin di default in corso...");

            Role adminRole = roleService.findRoleByName("ADMIN");


            accountService.saveAccount(defaultEmail, defaultPassword, Set.of(adminRole), true);

            System.out.println("Admin creato con successo!");
        } else {
            System.out.println("Account ADMIN già presente nel sistema.");
        }
    }

}
