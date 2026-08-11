package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.ShiftTaskTitleRequestDTO;
import giuliaciampa.YouRoster.dto.requests.ShiftTaskTitleUpdateRequestDTO;
import giuliaciampa.YouRoster.dto.responses.ShiftTaskTitleResponseDTO;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.services.ShiftTaskTitleService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shift-task-titles")
public class ShiftTaskTitleController {

    private final ShiftTaskTitleService shiftTaskTitleService;

    public ShiftTaskTitleController(ShiftTaskTitleService shiftTaskTitleService) {
        this.shiftTaskTitleService = shiftTaskTitleService;
    }

    //1. CREA NUOVO TITOLO(COORDINATOR, ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDINATOR')")
    public ShiftTaskTitleResponseDTO saveNewTaskTitle(@RequestBody @Validated ShiftTaskTitleRequestDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return shiftTaskTitleService.saveNewTaskTitle(payload);
    }

    //2. MODIFICA TITOLO (COORDINATOR, ADMIN)
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDINATOR')")
    public ShiftTaskTitleResponseDTO updateTaskTitle(@PathVariable UUID id, @RequestBody @Validated ShiftTaskTitleUpdateRequestDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return shiftTaskTitleService.updateTaskTitle(id, payload);
    }


    //3. DISATTIVA TITOLO (COORDINATOR, ADMIN)
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDINATOR')")
    public ShiftTaskTitleResponseDTO deactivateTaskTitle(@PathVariable UUID id) {
        return shiftTaskTitleService.deactivateTaskTitle(id);
    }

    //4. RIATTIVA TITOLO (COORDINATOR, ADMIN)
    @PatchMapping("/{id}/reactivate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDINATOR')")
    public ShiftTaskTitleResponseDTO reactivateTaskTitle(@PathVariable UUID id) {
        return shiftTaskTitleService.reactivateTaskTitle(id);
    }


}
