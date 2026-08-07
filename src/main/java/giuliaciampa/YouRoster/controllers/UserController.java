package giuliaciampa.YouRoster.controllers;


import giuliaciampa.YouRoster.dto.requests.UpdateUserProfileDTO;
import giuliaciampa.YouRoster.dto.responses.UserProfileResponseDTO;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.services.UserService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. GET PROFILO UTENTE LOGGATO
    @GetMapping("/me")
    public UserProfileResponseDTO getMyProfile(@AuthenticationPrincipal Account currentAccount) {
        return userService.getMyProfile(currentAccount);
    }

    //2. AGGIORNA PROFILO UTENTE LOGGATO
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfileResponseDTO updateMyProfile(
            @AuthenticationPrincipal Account currentAccount,
            @ModelAttribute @Validated UpdateUserProfileDTO payload, BindingResult validationResult
    ) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return userService.updateProfile(currentAccount, payload);
    }

}
