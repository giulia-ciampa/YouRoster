package giuliaciampa.YouRoster.controllers;

import giuliaciampa.YouRoster.dto.requests.LoginRequestDTO;
import giuliaciampa.YouRoster.dto.requests.RefreshTokenRequestDTO;
import giuliaciampa.YouRoster.dto.requests.UpdateCredentialsRequestDTO;
import giuliaciampa.YouRoster.dto.requests.UserRegistrationRequestDTO;
import giuliaciampa.YouRoster.dto.responses.LoginResponseDTO;
import giuliaciampa.YouRoster.dto.responses.UpdateCredentialsResponseDTO;
import giuliaciampa.YouRoster.dto.responses.UserRegistrationResponseDTO;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.exceptions.UnauthorizedException;
import giuliaciampa.YouRoster.exceptions.ValidationException;
import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //1. REGISTRAZIONE
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


    //2.LOGIN
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

    //3.REFRESH TOKEN
    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@RequestBody @Validated RefreshTokenRequestDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }

        return authService.refreshToken(payload.refreshToken());
    }

    //4. CAMBIA EMAIL, CAMBIA PASSWORD, O EMAIL E PASSWORD
    @PatchMapping("/credentials")
    public UpdateCredentialsResponseDTO updateCredentials(
            @AuthenticationPrincipal Account currentAccount,
            @RequestBody @Validated UpdateCredentialsRequestDTO payload,
            BindingResult validationResult) {

        if (currentAccount == null) {
            throw new UnauthorizedException("Sessione non valida o utente non autenticato");
        }

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorsList);
        }
        
        return authService.updateCredentials(currentAccount.getId(), payload);

    }

}
