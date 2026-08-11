package giuliaciampa.YouRoster.services;

import com.cloudinary.Cloudinary;
import giuliaciampa.YouRoster.dto.requests.UpdateUserProfileDTO;
import giuliaciampa.YouRoster.dto.responses.UserProfileResponseDTO;
import giuliaciampa.YouRoster.emailTemplates.EmailTemplateBuilder;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.Role;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.AlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.BadRequestException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final EmailService emailService;
    private final AccountService accountService;


    @Value("${profileUpdated.url}")
    private String ProfileUpdated;


    public UserService(UserRepository userRepository, Cloudinary cloudinary, EmailService emailService, AccountService accountService) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.emailService = emailService;
        this.accountService = accountService;
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

    //METODO CARICAMENTO DOCUMENTI PER AGGIORNAMENTO
    public String uploadOptionalDocument(MultipartFile file, String documentName) {

        if (file == null || file.isEmpty()) {
            return null;
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

    //FIND BY ID
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id " + id + " non è stato trovato."));
    }


    //-------------------------------------------------------------------------------------------------------------
    //HELPER
    private UserProfileResponseDTO mapToDTO(User user, Account account) {
        String officeName = (user.getReferenceOffice() != null)
                ? user.getReferenceOffice().getName()
                : "Nessuna sede assegnata";

        Set<String> roleNames = account.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserProfileResponseDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getDateOfBirth(),
                user.getPlaceOfBirth(),
                user.getPhoneNumber(),
                user.getTaxCode(),
                user.getPhotoUrl(),
                user.getStreetAddress(),
                user.getHouseNumber(),
                user.getZipCode(),
                user.getCity(),
                user.getProvince(),
                user.getIban(),
                user.getDocumentNumber(),
                user.getDocumentType(),
                user.getIssueDate(),
                user.getExpirationDate(),
                user.getDocumentFrontUrl(),
                user.getDocumentBackUrl(),
                user.getTaxCodeCardFrontUrl(),
                user.getTaxCodeCardBackUrl(),
                officeName,
                roleNames
        );
    }


    //1. VISUALIZZA PROPRIO PROFILO
    @Transactional
    public UserProfileResponseDTO getMyProfile(Account currentAccount) {
        if (currentAccount.getUser() == null) {
            throw new NotFoundException("Nessun profilo utente associato a questo account!");
        }

        User user = userRepository.findByIdWithOffice(currentAccount.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Profilo utente non trovato"));


        return mapToDTO(user, currentAccount);

    }

    //2. MODIFICA DATI PROFILO, INVIA EMAIL SE MODIFICA DATI SENSIBILI
    @Transactional
    public UserProfileResponseDTO updateProfile(Account currentAccount, UpdateUserProfileDTO payload) {
        if (currentAccount.getUser() == null) {
            throw new NotFoundException("Nessun profilo utente è associato a questo account");
        }


        //1. recupero l'utente
        User user = userRepository.findByIdWithOffice(currentAccount.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Profilo utente non trovato"));

        //2. tracciamento cambio iban o documenti per inviare email
        boolean ibanChanged = false;
        boolean documentDataChanged = false;

        //3. aggiornamento opzionale dei campi di testo non sensibili
        if (payload.phoneNumber() != null) user.setPhoneNumber(payload.phoneNumber());
        if (payload.streetAddress() != null) user.setStreetAddress(payload.streetAddress());
        if (payload.houseNumber() != null) user.setHouseNumber(payload.houseNumber());
        if (payload.zipCode() != null) user.setZipCode(payload.zipCode());

        //4. aggiornamento opzionale dei campi di testo sensibili
        if (payload.iban() != null && !Objects.equals(payload.iban(), user.getIban())) {
            user.setIban(payload.iban());
            ibanChanged = true;
        }

        if (payload.documentNumber() != null && !Objects.equals(payload.documentNumber(), user.getDocumentNumber())) {
            user.setDocumentNumber(payload.documentNumber());
            documentDataChanged = true;
        }

        if (payload.documentType() != null && !Objects.equals(payload.documentType(), user.getDocumentType())) {
            user.setDocumentType(payload.documentType());
            documentDataChanged = true;
        }

        if (payload.issueDate() != null && !Objects.equals(payload.issueDate(), user.getIssueDate())) {
            user.setIssueDate(payload.issueDate());
            documentDataChanged = true;
        }

        if (payload.expirationDate() != null && !Objects.equals(payload.expirationDate(), user.getExpirationDate())) {
            user.setExpirationDate(payload.expirationDate());
            documentDataChanged = true;
        }


        //5. CONTROLLO OBBLIGATORIETÀ FOTO: Se i dati del documento sono cambiati, bisogna inviare anche i relativi file

        if (documentDataChanged) {
            boolean hasFront = payload.documentFront() != null && !payload.documentFront().isEmpty();
            boolean hasBack = payload.documentBack() != null && !payload.documentBack().isEmpty();

            if (!hasFront || !hasBack) {
                throw new BadRequestException("Se modifichi i dati del documento, devi allegare anche le nuove foto di Fronte e Retro.");
            }
        }


        // 6. Caricamento opzionale dei campi file su Cloudinary
        String newAvatarUrl = uploadOptionalDocument(payload.photoUrl(), "Foto profilo");
        String newFrontUrl = uploadOptionalDocument(payload.documentFront(), "Fronte documento");
        String newBackUrl = uploadOptionalDocument(payload.documentBack(), "Retro Documento");
        String newTaxFrontUrl = uploadOptionalDocument(payload.taxCodeCardFront(), "Fronte Codice Fiscale");
        String newTaxBackUrl = uploadOptionalDocument(payload.taxCodeCardBack(), "Retro Codice Fiscale");

        // 7. Assegnazione degli URL restituiti da Cloudinary all'entità User
        boolean documentFilesChanged = false;

        if (newAvatarUrl != null && !Objects.equals(newAvatarUrl, user.getPhotoUrl())) {
            user.setPhotoUrl(newAvatarUrl);
        }

        if (newFrontUrl != null && !Objects.equals(newFrontUrl, user.getDocumentFrontUrl())) {
            user.setDocumentFrontUrl(newFrontUrl);
            documentFilesChanged = true;
        }

        if (newBackUrl != null && !Objects.equals(newBackUrl, user.getDocumentBackUrl())) {
            user.setDocumentBackUrl(newBackUrl);
            documentFilesChanged = true;
        }

        if (newTaxFrontUrl != null && !Objects.equals(newTaxFrontUrl, user.getTaxCodeCardFrontUrl())) {
            user.setTaxCodeCardFrontUrl(newTaxFrontUrl);
            documentFilesChanged = true;
        }

        if (newTaxBackUrl != null && !Objects.equals(newTaxBackUrl, user.getTaxCodeCardBackUrl())) {
            user.setTaxCodeCardBackUrl(newTaxBackUrl);
            documentFilesChanged = true;
        }

        boolean documentChanged = documentDataChanged || documentFilesChanged;

        //8. Salvo l'utente aggiornato
        User updatedUser = userRepository.save(user);

        //9. invio email solo se variano dati sensibili
        if (ibanChanged || documentChanged) {
            List<String> recipientEmails = accountService.getEmailsByRoles(
                    List.of("ADMIN", "HR", "AP E PAYROLL SPECIALIST")
            );

            if (!recipientEmails.isEmpty()) {
                String employeeName = updatedUser.getName() + " " + updatedUser.getSurname();
                String details = (ibanChanged && documentChanged) ? "l'IBAN e i documenti"
                        : ibanChanged ? "l'IBAN" : "i documenti";

                String htmlBody = EmailTemplateBuilder.buildUpdateSensitiveData(employeeName, details, ProfileUpdated);
                String subject = "[YouRoster] Modifica Dati Sensibili: " + employeeName;

                for (String email : recipientEmails) {
                    try {
                        emailService.sendHtmlEmail(email, subject, htmlBody);
                    } catch (Exception e) {
                        System.err.println("Errore invio mail a " + email + ": " + e.getMessage());
                    }
                }
            }
        }
        return mapToDTO(updatedUser, currentAccount);
    }

    //3. GET TUTTI GLI UTENTI E LE SUE INFORMAZIONI(ADMIN, HR, PAYROLL)
    @Transactional
    public UserProfileResponseDTO getUserProfileForManagement(UUID userId) {
        User user = userRepository.findByIdWithOffice(userId).orElseThrow(() -> new NotFoundException("L'utente con id " + userId + " non è stato trovato."));
        Account account = user.getAccount();
        if (account == null) {
            throw new NotFoundException("Nessun account associato all'utente con id " + userId);
        }
        return mapToDTO(user, account);
    }


}


