package marcozagaria.app_viaggi_2.repositories;

import marcozagaria.app_viaggi_2.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    Optional<Prenotazione> findByDipendenteIdAndDataDiRichiesta(UUID utenteId, LocalDate data);

    Optional<Prenotazione> findByDipendenteIdAndViaggioId(UUID utenteId, UUID viaggioId);
}
