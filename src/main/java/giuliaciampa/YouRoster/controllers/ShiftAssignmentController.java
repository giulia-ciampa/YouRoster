package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.ShiftAssignmentDTO;
import giuliaciampa.YouRoster.dto.requests.UpdateShiftAssignmentDTO;
import giuliaciampa.YouRoster.dto.responses.ShiftAssignmentResponseDTO;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.services.ShiftAssignmentService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shift-assignment")
public class ShiftAssignmentController {

    private final ShiftAssignmentService shiftAssignmentService;

    public ShiftAssignmentController(ShiftAssignmentService shiftAssignmentService) {
        this.shiftAssignmentService = shiftAssignmentService;
    }

    //1. CREA UN'ASSEGNAZIONE
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public ShiftAssignmentResponseDTO saveNewAssignment(@RequestBody @Validated ShiftAssignmentDTO payload, BindingResult validationResult) {


        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return shiftAssignmentService.saveNewAssignment(payload);
    }

    //2. MODIFICA UN'ASSEGNAZIONE(INVIO EMAIL ALLO USER)
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public ShiftAssignmentResponseDTO updateShiftAssignment(
            @PathVariable UUID id,
            @RequestBody @Validated UpdateShiftAssignmentDTO payload,
            BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return shiftAssignmentService.updateShiftAssignment(id, payload);
    }

    //3. CANCELLA UN'ASSEGNAZIONE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public void deleteShiftAssignment(@PathVariable UUID id) {

        shiftAssignmentService.deleteShiftAssignment(id);

    }

    //4. VISUALIZZA TUTTE LE ASSEGNAZIONI
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public Page<ShiftAssignmentResponseDTO> getAllAssignment(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "15") int size,
                                                             @RequestParam(defaultValue = "shiftDate") String sortBy) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return shiftAssignmentService.getAllAssignment(pageable);
    }

    //5. VISUALIZZA LE ASSEGNAZIONI PER DATA
    @GetMapping("by-date")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public Page<ShiftAssignmentResponseDTO> getAllAssignmentByDate(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "15") int size,
                                                                   @RequestParam(defaultValue = "shiftDate") String sortBy,
                                                                   @RequestParam(required = false) LocalDate shiftDate) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return shiftAssignmentService.getAllAssignmentByDate(shiftDate, pageable);
    }

    //6. VISUALIZZA LE ASSEGNAZIONI DA DATA X A DATA Y
    @GetMapping("/between-dates")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHIFT MANAGER')")
    public Page<ShiftAssignmentResponseDTO> getAssignmentsBetweenDates(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "15") int size,
                                                                       @RequestParam(defaultValue = "shiftDate") String sortBy,
                                                                       @RequestParam(required = false) LocalDate startDate,
                                                                       @RequestParam(required = false) LocalDate endDate) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return shiftAssignmentService.getAssignmentsBetweenDates(startDate, endDate, pageable);
    }

    //7. VISUALIZZA LE PROPRIE ASSEGNAZIONI
    @GetMapping("{userId}")
    public Page<ShiftAssignmentResponseDTO> getMyAssignment(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "shiftDate") String sortBy,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return shiftAssignmentService.getMyAssignment(userId, startDate, endDate, pageable);
    }


    //8. VISUALIZZA UTENTI IN TURNO CON TE
    @GetMapping("{userId}/same-shift")
    public List<ShiftAssignmentResponseDTO> getColleaguesOnMyShift(@PathVariable UUID userId, @RequestParam LocalDate shiftDate) {
        return shiftAssignmentService.getColleaguesOnMyShift(userId, shiftDate);
    }


}



