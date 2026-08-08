package giuliaciampa.YouRoster.entities;


import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "offices")
public class Office {

    //ATTRIBUTI
    @Id
    @GeneratedValue
    @Column(name = "office_id")
    private UUID id;

    @Column(name = "office_name", nullable = false, unique = true, length = 40)
    private String name;

    @Column(nullable = false, length = 40)
    private String street;

    @Column(name = "house_number", nullable = false, length = 10)
    private String houseNumber;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Column(nullable = false, length = 20)
    private String city;

    @Column(nullable = false, length = 2)
    private String province;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "office_status", nullable = false)
    private OfficeStatus status = OfficeStatus.ACTIVE;

    //COSTRUTTORE
    public Office(String name, String street, String houseNumber, String zipCode, String city, String province, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.province = province;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    //COSTRUTTORE VUOTO

    public Office() {
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public OfficeStatus getStatus() {
        return status;
    }

    public void setStatus(OfficeStatus status) {
        this.status = status;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    //TO STRING
    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", status=" + status +
                '}';
    }
}
