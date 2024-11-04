package marcozagaria.app_viaggi_2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        String note,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La data deve essere nel formato yyyy-MM-dd")
        LocalDate data_di_richiesta,
        @NotEmpty(message = "Il dipendente è obbligatorio!")
        @Size(min = 2, max = 40, message = "lo stato deve essere compreso tra 2 e 40 caratteri!")
        UUID dipendente_id,
        @NotEmpty(message = "Il viaggio è obbligatorio!")
        @Size(min = 2, max = 40, message = "lo stato deve essere compreso tra 2 e 40 caratteri!")
        UUID viaggio_id) {
}
