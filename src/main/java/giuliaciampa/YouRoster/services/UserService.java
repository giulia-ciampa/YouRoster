package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.AlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //CONTROLLA SE L'UTENTE GIA' ESISTE DAL CODICE FISCALE
    public String checkIfTaxCodeAlreadyExists(String taxCode) {

        String cleanTaxCode = taxCode.trim().toUpperCase();
        if (userRepository.existsByTaxCode(cleanTaxCode)) {
            throw new AlreadyExistsException("L'utente con il codice fiscale " + cleanTaxCode + " è già esistente");
        }

        return cleanTaxCode;
    }

    //CONTROLLA SE L'UTENTE GIA' ESISTE DAL NUMERO DI DOCUMENTO
    public String checkIfDocumentNumberAlreadyExists(String documentNumber) {

        String cleanDocumentNumber = documentNumber.trim().toUpperCase();
        if (userRepository.existsByDocumentNumber(cleanDocumentNumber)) {
            throw new AlreadyExistsException("L'utente con il numero di documento " + cleanDocumentNumber + " è già esistente");
        }


        return cleanDocumentNumber;
    }

    //METODO PER SALVARE LO USER
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    //TROVA LO USER IN BASE ALL'ID DELL'ACCOUNT
    public User findByAccountId(UUID accountId) {
        return userRepository.findByAccount_Id(accountId).orElseThrow(() -> new NotFoundException("l'utente con account id " + accountId + " non è stato trovato"));
    }

    //CANCELLA LO USER PER POTER CANCELLARE L'ACCOUNT (ADMIN)
    public void deleteUser(UUID userId) {
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("L'utente con id " + userId + " non è stato trovato"));
        userRepository.delete(foundUser);
    }
}
