package marcozagaria.app_viaggi_2.controllers;

import marcozagaria.app_viaggi_2.entities.Viaggio;
import marcozagaria.app_viaggi_2.payloads.ViaggioDTO;
import marcozagaria.app_viaggi_2.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/viaggio")
public class ViaggioController {
    @Autowired
    private ViaggioService viaggioService;

    @GetMapping
    public Page<Viaggio> getViaggio(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return viaggioService.getAllViaggioList(page, size, sortBy);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio createBlogPOst(@RequestBody ViaggioDTO body) {
        return viaggioService.saveViaggio(body);
    }

    @GetMapping("/{Id}")
    public Viaggio cercaViaggioId(@PathVariable UUID Id) {
        return viaggioService.cercaId(Id);
    }

    @PutMapping("/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Viaggio cercaEModifica(@PathVariable UUID Id, @RequestBody ViaggioDTO body) {
        return viaggioService.cercaViaggioEModifica(Id, body);
    }

    @DeleteMapping("/{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void cercaECancella(@PathVariable UUID Id) {
        viaggioService.cercaViaggioECancella(Id);
    }
}
