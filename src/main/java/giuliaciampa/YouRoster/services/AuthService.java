package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.UserRegistrationRequestDTO;
import giuliaciampa.YouRoster.dto.responses.UserRegistrationResponseDTO;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.UserAlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.repositories.AccountRepository;
import giuliaciampa.YouRoster.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder bcrypt;

    public AuthService(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder bcrypt) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.bcrypt = bcrypt;
    }

    //METODO SALVA NUOVO ACCOUNT-REGISTER E CREA UNO USER

    @Transactional
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO payload) {

        //controlli
        if (accountRepository.existsByEmail(payload.email())) {
            throw new UserAlreadyExistsException("L'utente con email " + payload.email() + " è già esistente");
        }

        String cleanTaxCode = payload.taxCode().trim().toUpperCase();


        if (userRepository.existsByTaxCode(cleanTaxCode)) {
            throw new UserAlreadyExistsException("L'utente con il codice fiscale " + payload.taxCode() + " è già esistente");
        }


        String cleanDocumentNumber = payload.documentNumber().trim().toUpperCase();

        if (userRepository.existsByDocumentNumber(cleanDocumentNumber)) {
            throw new UserAlreadyExistsException("L'utente con il numero di documento " + payload.documentNumber() + " è già esistente");
        }

        if (!payload.password().equals(payload.confirmPassword())) {
            throw new ValidationException("Le password inserite non coincidono, riprova.");
        }

        //controllo documenti in base alla nazionalità
        String cleanNumber = payload.documentNumber().trim().toUpperCase();
        boolean isValid;

        // Se è cittadino italiano, possiamo applicare le regex rigide italiane
        if ("IT".equalsIgnoreCase(payload.nationality().trim()) || "ITALIA".equalsIgnoreCase(payload.nationality().trim())) {
            isValid = switch (payload.documentType()) {
                case IDENTITY_CARD -> cleanNumber.matches("^[A-Z]{2}[0-9]{5}[A-Z]{2}$"); // CIE italiana
                case DRIVING_LICENSE -> cleanNumber.matches("^[A-Z]{2}[0-9]{7}[A-Z]{1}$");
                case PASSPORT -> cleanNumber.matches("^[A-Z0-9]{5,20}$");
            };
        } else {
            // Per nazionalità estere, accettiamo un formato alfanumerico generico SEPA/Internazionale
            isValid = cleanNumber.matches("^[A-Z0-9]{5,20}$");
        }

        if (!isValid) {
            throw new ValidationException("Il formato del numero di documento non è valido per la nazionalità " + payload.nationality());

        }

        //creazione oggetti
        Account account = new Account();
        User user = new User();

        //account
        account.setEmail(payload.email());
        account.setPassword(bcrypt.encode(payload.password()));

        accountRepository.save(account);

        //user
        user.setName(payload.name());
        user.setSurname(payload.surname());
        user.setNationality(payload.nationality().trim().toUpperCase());
        user.setTaxCode(cleanTaxCode);
        user.setDateOfBirth(payload.dateOfBirth());
        user.setPlaceOfBirth(payload.placeOfBirth());
        user.setPhoneNumber(payload.phoneNumber());
        user.setAddress(payload.streetAddress());
        user.setHouseNumber(payload.houseNumber());
        user.setZipCode(payload.zipCode());
        user.setCity(payload.city());
        user.setProvince(payload.province());
        user.setPhotoUrl("https://ui-avatars.com/api/?name=" + payload.name() + payload.surname());
        user.setIban(payload.iban().trim().toUpperCase());
        user.setDocumentNumber(cleanNumber);
        user.setDocumentType(payload.documentType());
        user.setIssueDate(payload.issueDate());
        user.setExpirationDate(payload.expirationDate());
        user.setAccount(account);

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponseDTO(savedUser.getName(), savedUser.getSurname(), savedUser.getAccount().getEmail(), "Registrazione avvenuta con successo, presto l'amministratore potrà visualizzare la tua richiesta di attivazione.", LocalDateTime.now());
    }


    //METODO CHE CREA ACCOUNT ADMIN
//    public Account createAdminAccount(AccountLoginRequestDTO payload) {
//        //controllo se l'email non esiste già
//        if (!accountRepository.existsByEmail(payload.email())) {
//            Account newAccount = new Account();
//
//            newAccount.setEmail(payload.email());
//            newAccount.setPassword(payload.password());
//
//        }
//    }
}
