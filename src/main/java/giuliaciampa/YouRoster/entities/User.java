package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    //ATTRIBUTI
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String surname;

    @Column(nullable = false, unique = true, length = 16)
    private String taxCode;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne //VEDI IN SEGUITO SE FARE UNA RELAZIONE BIDIREZIONALE
    @JoinColumn(name = "reference_office_id")
    private Office referenceOffice;

    @Column(unique = true, nullable = false, length = 50)
    private String iban;

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    private Account account;

    //COSTRUTTORE

    public User(String name, String surname, String taxCode, LocalDate dateOfBirth, String placeOfBirth, String phoneNumber, String address, String photoUrl, Office referenceOffice, String iban, String documentNumber, DocumentType documentType, LocalDate issueDate, LocalDate expirationDate, Account account) {

        this.name = name;
        this.surname = surname;
        this.taxCode = taxCode;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photoUrl = photoUrl;
        this.referenceOffice = referenceOffice;
        this.iban = iban;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.account = account;
    }

    //COSTRUTTORE VUOTO

    protected User() {
    }

    //GETTER E SETTER

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Office getReferenceOffice() {
        return referenceOffice;
    }

    public void setReferenceOffice(Office referenceOffice) {
        this.referenceOffice = referenceOffice;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //TOSTRING


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", referenceOffice=" + referenceOffice +
                ", iban='" + iban + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentType=" + documentType +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", account=" + account +
                '}';
    }
}
