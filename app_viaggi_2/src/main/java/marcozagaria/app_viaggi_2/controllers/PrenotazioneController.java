package marcozagaria.app_viaggi_2.controllers;


import marcozagaria.app_viaggi_2.entities.Prenotazione;
import marcozagaria.app_viaggi_2.payloads.PrenotazioneDTO;
import marcozagaria.app_viaggi_2.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    public Page<Prenotazione> getPrenotazione(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        // Mettiamo dei valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return prenotazioneService.getAllPrenotazioneList(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody PrenotazioneDTO body) {
        return prenotazioneService.savePrenotazione(body);
    }

    @GetMapping("/{Id}")
    public Prenotazione createPrenotazioneId(@PathVariable UUID Id) {
        return prenotazioneService.cercaId(Id);
    }

    @PutMapping("/{Id}")
    public Prenotazione cercaEModifica(@PathVariable UUID Id, @RequestBody PrenotazioneDTO body) {
        return prenotazioneService.cercaPrenotazioneEModifica(Id, body);
    }

    @DeleteMapping("/{Id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void cercaECancella(@PathVariable UUID Id) {
        prenotazioneService.cercaPrenotazioneECancella(Id);
    }
}
