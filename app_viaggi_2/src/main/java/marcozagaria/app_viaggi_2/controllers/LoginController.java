package marcozagaria.app_viaggi_2.controllers;

import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.exeption.BadRequestException;
import marcozagaria.app_viaggi_2.payloads.DipendenteDTO;
import marcozagaria.app_viaggi_2.payloads.LoginDTO;
import marcozagaria.app_viaggi_2.payloads.LoginResponseDTO;
import marcozagaria.app_viaggi_2.services.DipendenteService;
import marcozagaria.app_viaggi_2.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/autorizzato")
public class LoginController {
    @Autowired
    DipendenteService dipendenteService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(loginService.controllaCredenziali(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        // @Validated serve per "attivare" le regole di validazione descritte nel DTO
        // BindingResult contiene l'esito della validazione, quindi sarà utile per capire se ci sono stati errori e quali essi siano
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }
        return dipendenteService.saveDipendente(body);
    }
}
