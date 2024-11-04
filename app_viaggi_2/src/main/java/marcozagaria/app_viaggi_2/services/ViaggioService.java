package marcozagaria.app_viaggi_2.services;


import marcozagaria.app_viaggi_2.entities.Viaggio;
import marcozagaria.app_viaggi_2.exeption.NotFoundException;
import marcozagaria.app_viaggi_2.payloads.ViaggioDTO;
import marcozagaria.app_viaggi_2.repositories.DipendenteRepository;
import marcozagaria.app_viaggi_2.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ViaggioService {
    @Autowired
    ViaggioRepository viaggioRepository;
    @Autowired
    DipendenteRepository dipendenteRepository;

    public Page<Viaggio> getAllViaggioList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return viaggioRepository.findAll(pageable);
    }

    public Viaggio saveViaggio(ViaggioDTO body) {
        Viaggio newViaggio = new Viaggio(body.destinazione(), body.data(), body.stato());
        return viaggioRepository.save(newViaggio);
    }


    public Viaggio cercaId(UUID id) {
        return viaggioRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Viaggio cercaViaggioEModifica(UUID id, ViaggioDTO body) {
        Viaggio cerca = cercaId(id);
        cerca.setStato(body.stato());
        if (cerca == null) throw new NotFoundException(id);
        return viaggioRepository.save(cerca);
    }

    public void cercaViaggioECancella(UUID id) {
        Viaggio cerca = cercaId(id);
        if (cerca == null) throw new NotFoundException(id);
        viaggioRepository.delete(cerca);
    }
}
