package giuliaciampa.YouRoster.services;

import com.cloudinary.Cloudinary;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.AlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;


    public UserService(UserRepository userRepository, Cloudinary cloudinary, Cloudinary cloudinary1) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary1;
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
//    public void deleteUser(UUID userId) {
//        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("L'utente con id " + userId + " non è stato trovato"));
//        userRepository.delete(foundUser);
//    }

    //METODO CARICAMENTO DOCUMENTI
    public String uploadDocuments(MultipartFile file, String documentName) {

        if (file == null || file.isEmpty()) {
            throw new ValidationException("Il caricamento del file per " + documentName + " è obbligatorio");
        }

        //controllo dimensione
        long maxSizeBytes = 6 * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new ValidationException("Il file " + documentName + " supera il limite massimo di 6 MB.");
        }

        //controllo tipo file, solo immagini o pdf
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ValidationException("Il nome del file per " + documentName + " non è valido.");
        }

        String lowerName = originalFilename.toLowerCase();

        boolean isValidExtension = lowerName.endsWith(".png") ||
                lowerName.endsWith(".jpeg") ||
                lowerName.endsWith(".jpg") ||
                lowerName.endsWith(".pdf");

        if (!isValidExtension) {
            throw new ValidationException("Il file " + documentName + " deve avere un'estensione valida (.png, .jpg, .jpeg, .pdf).");
        }

        // Controllo sul MIME Type per ulteriore sicurezza
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new ValidationException("Il formato del file " + documentName + " non è supportato.");
        }

        //upload del file su cloudinary
        try {
            Map<String, Object> uploadParams = Map.of(
                    "folder", "documents",
                    "quality", "auto" // Compressione automatica di Cloudinary
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento di " + documentName, e);
        }

    }
    
}
