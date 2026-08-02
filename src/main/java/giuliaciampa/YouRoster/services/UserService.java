package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.UserAlreadyExistsException;
import giuliaciampa.YouRoster.repositories.UserRepository;
import org.springframework.stereotype.Service;

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
            throw new UserAlreadyExistsException("L'utente con il codice fiscale " + cleanTaxCode + " è già esistente");
        }

        return cleanTaxCode;
    }

    //CONTROLLA SE L'UTENTE GIA' ESISTE DAL NUMERO DI DOCUMENTO
    public String checkIfDocumentNumberAlreadyExists(String documentNumber) {

        String cleanDocumentNumber = documentNumber.trim().toUpperCase();
        if (userRepository.existsByDocumentNumber(cleanDocumentNumber)) {
            throw new UserAlreadyExistsException("L'utente con il numero di documento " + cleanDocumentNumber + " è già esistente");
        }


        return cleanDocumentNumber;
    }

    //METODO PER SALVARE LO USER
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
