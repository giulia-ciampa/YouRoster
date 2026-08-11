package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.repositories.ShiftTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class ShiftTaskService {
    private final ShiftTaskRepository shiftTaskRepository;

    public ShiftTaskService(ShiftTaskRepository shiftTaskRepository) {
        this.shiftTaskRepository = shiftTaskRepository;
    }

    //1. CREA NUOVO TASK

    //2. MODIFICA TASK

    //3. ELIMINA TASK

    //4. GET ALL PER UNO SPECIFICO TURNO

    //5. GET ALL GENERICO

    //6. GET TUTTI I TASK ASSEGNATI NEL GIORNO IN QUELL'UFFICIO

}
