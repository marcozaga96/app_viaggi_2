package marcozagaria.app_viaggi_2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(@NotEmpty(message = "La destinazione è obbligatorio!")
                         @Size(min = 2, max = 40, message = "La destinazione deve essere compreso tra 2 e 40 caratteri!")
                         String destinazione,
                         @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La data deve essere nel formato yyyy-MM-dd")
                         LocalDate data,
                         @NotEmpty(message = "Lo stato è obbligatorio!")
                         @Size(min = 2, max = 40, message = "lo stato deve essere compreso tra 2 e 40 caratteri!")
                         String stato) {
}
