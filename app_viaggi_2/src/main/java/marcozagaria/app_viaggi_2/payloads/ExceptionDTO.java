package marcozagaria.app_viaggi_2.payloads;

import java.time.LocalDateTime;

public record ExceptionDTO(String message, LocalDateTime timestamp) {
}
