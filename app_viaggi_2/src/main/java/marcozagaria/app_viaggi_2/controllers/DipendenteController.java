package marcozagaria.app_viaggi_2.controllers;

import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.payloads.DipendenteDTO;
import marcozagaria.app_viaggi_2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/* *************************************************** USERS CRUD ****************************************************
1. GET http://localhost:3001/users
2. POST http://localhost:3001/users (+ payload)
3. GET http://localhost:3001/users/{userId}
4. PUT http://localhost:3001/users/{userId} (+ payload)
5. DELETE http://localhost:3001/users/{userId}
*/


@RestController
@RequestMapping("/dipendente")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    // <-- Solo gli ADMIN possono visualizzare la lista degli utenti in questa app
    public Page<Dipendente> getDipendente(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        // Mettiamo dei valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return dipendenteService.getAllDipendenteList(page, size, sortBy);
    }

    // ************************************************* /ME ENDPOINTS ***********************************************
    // Se ho effettuato SecurityContextHolder.getContext().setAuthentication(authentication) nel Filter, allora negli endpoint autenticati
    // posso accedere a chi è l'utente che sta effettuando la richiesta, tramite @AuthenticationPrincipal. Grazie a questo Principal quindi
    // possiamo andare ad implementare tutta una serie di endpoint "personali", cioè endpoint per leggere il proprio profilo, cambiare i propri
    // dati oppure anche cancellare se stessi. Inoltre grazie al Principal potremo in futuro anche andare ad effettuare dei controlli, es:
    // endpoint per cancellare un record di cui sono proprietario, devo fare una verifica che il proprietario corrisponda al Principal
    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentDipendente) {
        return currentDipendente;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentDipendente, @RequestBody @Validated DipendenteDTO body) {
        return dipendenteService.cercaDipendenteEModifica(currentDipendente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentDipendente) {
        dipendenteService.cercaDipendenteECancella(currentDipendente.getId());
    }


    @GetMapping("/{Id}")
    public Dipendente cercaDipendenteId(@PathVariable UUID Id) {
        return dipendenteService.cercaId(Id);
    }

    @PutMapping("/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    // <-- Solo gli ADMIN possono visualizzare la lista degli utenti in questa app
    public Dipendente cercaEModifica(@PathVariable UUID Id, @RequestBody DipendenteDTO body) {
        return dipendenteService.cercaDipendenteEModifica(Id, body);
    }

    @DeleteMapping("/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void cercaECancella(@PathVariable UUID Id) {
        dipendenteService.cercaDipendenteECancella(Id);
    }

    @PatchMapping("/{Id}/avatar")
    @PreAuthorize("hasAuthority('USER')")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID Id) {
        // "avatar" deve corrispondere ESATTAMENTE al campo del FormData che ci invia il Frontend
        // Se non corrisponde, non troverò il file
        return dipendenteService.uploadAvatar(file, Id);
    }
}
