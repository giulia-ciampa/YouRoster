package giuliaciampa.YouRoster.runners;

import giuliaciampa.YouRoster.services.OfficeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class OfficesInitializer implements CommandLineRunner {

    private final OfficeService officeService;

    public OfficesInitializer(OfficeService officeService) {
        this.officeService = officeService;
    }


    @Override
    public void run(String... args) throws Exception {
        officeService.createDefaultOffices("NAVONA", "Piazza Navona 1", LocalTime.of(9, 0), LocalTime.of(19, 0));
        officeService.createDefaultOffices("COLOSSEO", "Piazza Colosseo 1", LocalTime.of(8, 0), LocalTime.of(18, 0));
        officeService.createDefaultOffices("VATICANO 1", "Via Vaticano 1", LocalTime.of(8, 0), LocalTime.of(19, 0));
        officeService.createDefaultOffices("VATICANO 2", "Via Vaticano 2", LocalTime.of(8, 0), LocalTime.of(19, 0));
        officeService.createDefaultOffices("VATICANO 3", "Via Vaticano 3", LocalTime.of(8, 0), LocalTime.of(19, 0));

        officeService.createDefaultOffices("NAVONA", "Piazza Navona 1", LocalTime.of(9, 0), LocalTime.of(19, 0));

        System.out.println("sedi salvate correttamente all'avvio");
    }
}
