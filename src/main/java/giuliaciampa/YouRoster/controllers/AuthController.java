package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.LoginRequestDTO;
import giuliaciampa.YouRoster.dto.requests.UserRegistrationRequestDTO;
import giuliaciampa.YouRoster.dto.responses.LoginResponseDTO;
import giuliaciampa.YouRoster.dto.responses.UserRegistrationResponseDTO;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //REGISTRAZIONE
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDTO register(@RequestBody @Validated UserRegistrationRequestDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return authService.registerUser(payload);
    }


    //LOGIN
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginRequestDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return authService.login(payload);
    }

}
