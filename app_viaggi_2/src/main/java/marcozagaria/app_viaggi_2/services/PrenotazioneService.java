package marcozagaria.app_viaggi_2.services;

import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.entities.Prenotazione;
import marcozagaria.app_viaggi_2.entities.Viaggio;
import marcozagaria.app_viaggi_2.exeption.NotFoundException;
import marcozagaria.app_viaggi_2.payloads.PrenotazioneDTO;
import marcozagaria.app_viaggi_2.repositories.DipendenteRepository;
import marcozagaria.app_viaggi_2.repositories.PrenotazioneRepository;
import marcozagaria.app_viaggi_2.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    PrenotazioneRepository prenotazioneRepository;
    @Autowired
    ViaggioRepository viaggioRepository;
    @Autowired
    DipendenteRepository dipendenteRepository;

    public Page<Prenotazione> getAllPrenotazioneList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione savePrenotazione(PrenotazioneDTO body) {
        Optional<Viaggio> viaggio = viaggioRepository.findById(body.viaggio_id());
        if (!viaggio.isPresent()) {
            throw new RuntimeException("Viaggio non trovato con ID: " + body.viaggio_id());
        }
        Viaggio viaggio1 = viaggio.get();

        Optional<Dipendente> dipendente = dipendenteRepository.findById(body.dipendente_id());
        if (!dipendente.isPresent()) {
            throw new RuntimeException("Dipendente non trovato con ID: " + body.dipendente_id());
        }
        Dipendente dipendente1 = dipendente.get();

        Optional<Prenotazione> prenotazioniDipendentePerData = prenotazioneRepository.findByDipendenteIdAndDataDiRichiesta(body.dipendente_id(), body.data_di_richiesta());
        if (!prenotazioniDipendentePerData.isEmpty()) {
            throw new RuntimeException("L'utente ha già una prenotazione per questa data.");
        }
        Optional<Prenotazione> prenotazioniDipendente = prenotazioneRepository.findByDipendenteIdAndViaggioId(body.dipendente_id(), body.viaggio_id());
        if (!prenotazioniDipendente.isEmpty()) {
            throw new RuntimeException("L'utente ha già una prenotazione.");
        }

        Prenotazione newPrenotazione = new Prenotazione(body.note(), body.data_di_richiesta());
        newPrenotazione.setViaggio(viaggio1);
        newPrenotazione.setDipendente(dipendente1);
        return prenotazioneRepository.save(newPrenotazione);
    }


    public Prenotazione cercaId(UUID id) {

        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Prenotazione cercaPrenotazioneEModifica(UUID id, PrenotazioneDTO body) {
        Prenotazione cerca = cercaId(id);
        cerca.setNote(body.note());
        cerca.setDataDiRichiesta(body.data_di_richiesta());
        if (cerca == null) throw new NotFoundException(id);
        return prenotazioneRepository.save(cerca);
    }

    public void cercaPrenotazioneECancella(UUID id) {
        Prenotazione cerca = cercaId(id);
        if (cerca == null) throw new NotFoundException(id);
        prenotazioneRepository.delete(cerca);
    }
}
