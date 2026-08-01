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

    @Column(name = "office_name", nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    //COSTRUTTORE
    public Office(String name, String address, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    //COSTRUTTORE VUOTO

    protected Office() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    //TO STRING
    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }
}
