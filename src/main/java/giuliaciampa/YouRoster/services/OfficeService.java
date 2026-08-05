package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.entities.Office;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.OfficeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.UUID;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    //CREA SEDI DI DEFAULT
    public void createDefaultOffices(String name, String address, LocalTime openingTime, LocalTime closingTime) {
        //1. controllo che la sede non esiste già

        if (officeRepository.existsByName(name)) {
            return;
        }

        //2. creo la sede
        Office newOffice = new Office();
        newOffice.setAddress(address);
        newOffice.setName(name);
        newOffice.setOpeningTime(openingTime);
        newOffice.setClosingTime(closingTime);

        officeRepository.save(newOffice);
    }

    //FIND BY ID
    public Office findById(UUID id) {
        return officeRepository.findById(id).orElseThrow(() -> new NotFoundException("L'ufficio con id " + id + " non è stato trovato"));
    }
}
