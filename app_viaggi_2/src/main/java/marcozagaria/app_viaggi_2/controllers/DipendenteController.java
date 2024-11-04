package marcozagaria.app_viaggi_2.controllers;

import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.payloads.DipendenteDTO;
import marcozagaria.app_viaggi_2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public Page<Dipendente> getDipendente(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        // Mettiamo dei valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return dipendenteService.getAllDipendenteList(page, size, sortBy);
    }
    

    @GetMapping("/{Id}")
    public Dipendente cercaDipendenteId(@PathVariable UUID Id) {
        return dipendenteService.cercaId(Id);
    }

    @PutMapping("/{Id}")
    public Dipendente cercaEModifica(@PathVariable UUID Id, @RequestBody DipendenteDTO body) {
        return dipendenteService.cercaDipendenteEModifica(Id, body);
    }

    @DeleteMapping("/{Id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void cercaECancella(@PathVariable UUID Id) {
        dipendenteService.cercaDipendenteECancella(Id);
    }

    @PatchMapping("/{Id}/avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID Id) {
        // "avatar" deve corrispondere ESATTAMENTE al campo del FormData che ci invia il Frontend
        // Se non corrisponde, non troverò il file
        return dipendenteService.uploadAvatar(file, Id);
    }
}
